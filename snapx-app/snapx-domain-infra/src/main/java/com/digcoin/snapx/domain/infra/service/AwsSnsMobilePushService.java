package com.digcoin.snapx.domain.infra.service;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.domain.infra.config.AwsSnsProperties;
import com.digcoin.snapx.domain.infra.constant.AwsSnsPushChannel;
import com.digcoin.snapx.domain.infra.entity.AwsSnsEndpoint;
import com.digcoin.snapx.domain.infra.mapper.AwsSnsEndpointMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsSnsMobilePushService {

    private final AwsSnsEndpointMapper awsSnsEndpointMapper;
    private final AwsSnsProperties awsSnsProperties;
    private final SnsClient snsClient;
    private final IdentifierGenerator identifierGenerator;
    private final ObjectMapper objectMapper;

    public long registerDevice(Long memberId, String platform, String deviceToken) {
        AwsSnsProperties.TopicArn topic = awsSnsProperties.getTopic();
        String globalNotificationArn = topic.getGlobalNotification();
        AwsSnsProperties.PlatformApp platformApp = getPlatformAppArn(platform);
        if (Objects.isNull(platformApp)) {
            throw CommonError.PARAMETER_ERROR.withMessage("platform not exist");
        }
        AwsSnsEndpoint awsSnsEndpoint = new AwsSnsEndpoint();
        awsSnsEndpoint.setId(identifierGenerator.nextId(awsSnsEndpoint).longValue());
        awsSnsEndpoint.setMemberId(memberId);
        awsSnsEndpoint.setPlatform(platformApp.getName());
        awsSnsEndpoint.setPlatformAppArn(platformApp.getArn());
        awsSnsEndpoint.setPushChannel(platformApp.getPushChannel());
        awsSnsEndpoint.setDeviceToken(deviceToken);

        // 唯一键 platformAppArn + pushChanel + deviceToken
        // 唯一键冲突时，更新memberId，updateTime。当同一个设备上登录其他用户时memberId就会更新
        awsSnsEndpointMapper.saveOrUpdateAwsSnsEndpoint(awsSnsEndpoint);

        // 调用AWS SNS API创建endpoint，幂等接口，同一设备不会重复创建
        String endpointArn = createEndpoint(platformApp.getArn(), deviceToken);
        // 绑定全局通知订阅
//        confirmSubscription(globalNotificationArn, deviceToken);
        // 更新保存endpointArn，endpointArn可能会变化。
        awsSnsEndpointMapper.update(new AwsSnsEndpoint(), Wrappers.lambdaUpdate(AwsSnsEndpoint.class)
                .set(AwsSnsEndpoint::getEndpointArn, endpointArn)
                .eq(AwsSnsEndpoint::getPlatformAppArn, awsSnsEndpoint.getPlatformAppArn())
                .eq(AwsSnsEndpoint::getPushChannel, awsSnsEndpoint.getPushChannel())
                .eq(AwsSnsEndpoint::getDeviceToken, awsSnsEndpoint.getDeviceToken())
        );

        return awsSnsEndpoint.getId();
    }

    private AwsSnsProperties.PlatformApp getPlatformAppArn(String platform) {
        Map<String, AwsSnsProperties.PlatformApp> map = awsSnsProperties.getPlatformApplicationArn();
        return map.get(platform);
    }

    private String createEndpoint(String platformApplicationArn, String token) {
        try {
            CreatePlatformEndpointRequest endpointRequest = CreatePlatformEndpointRequest.builder()
                    .token(token)
                    .platformApplicationArn(platformApplicationArn)
                    .build();

            CreatePlatformEndpointResponse response = snsClient.createPlatformEndpoint(endpointRequest);

            GetEndpointAttributesResponse endpointAttributes = snsClient.getEndpointAttributes(GetEndpointAttributesRequest.builder()
                    .endpointArn(response.endpointArn())
                    .build());
            log.info("endpointAttributes:[{}]", endpointAttributes.attributes());
            String enabled = endpointAttributes.attributes().get("Enabled");
            if ("false".equalsIgnoreCase(enabled)) {
                SetEndpointAttributesResponse setEndpointAttributesResponse = snsClient.setEndpointAttributes(SetEndpointAttributesRequest.builder()
                        .endpointArn(response.endpointArn())
                        .attributes(Map.of("Enabled", "true")).build());
                log.info("setEndpointAttributesResponse:[{}]", setEndpointAttributesResponse);
            }

            return response.endpointArn();
        } catch (SnsException e) {
            log.error("createEndpoint token:[{}] platformApplicationArn:[{}]", token, platformApplicationArn, e);
            throw CommonError.UNEXPECT_ERROR.withDefaults(e);
        }
    }

    public void confirmSubscription(String topicArn, String token) {
        try {
            ConfirmSubscriptionRequest request = ConfirmSubscriptionRequest.builder()
                    .token(token)
                    .topicArn(topicArn)
                    .build();

            ConfirmSubscriptionResponse result = snsClient.confirmSubscription(request);
            log.info("confirmSubscription statusCode:[{}] statusText:[{}] subscriptionArn:[{}]",
                    result.sdkHttpResponse().statusCode(), result.sdkHttpResponse().statusText(), result.subscriptionArn());
        } catch (SnsException e) {
            log.error("confirmSubscription token:[{}] topicArn:[{}]", token, topicArn, e);
            throw CommonError.UNEXPECT_ERROR.withDefaults(e);
        }
    }

    public void publish(Long memberId, String message) {
        publish(memberId, message, Collections.emptyMap());
    }

    public void publish(Long memberId, String message, Map<String, Object> data) {
        List<AwsSnsEndpoint> awsSnsEndpointList = awsSnsEndpointMapper.selectList(
                Wrappers.lambdaQuery(AwsSnsEndpoint.class).eq(AwsSnsEndpoint::getMemberId, memberId));
        publish(awsSnsEndpointList, message, data);
    }

    public void publish(List<AwsSnsEndpoint> awsSnsEndpointList, String message) {
        publish(awsSnsEndpointList, message, Collections.emptyMap());
    }

    public void publish(List<AwsSnsEndpoint> awsSnsEndpointList, String message, Map<String, Object> data) {
        for (AwsSnsEndpoint awsSnsEndpoint : awsSnsEndpointList) {
            if (StringUtils.isBlank(awsSnsEndpoint.getEndpointArn())) {
                continue;
            }
            try {

                PublishRequest publishRequest;
                if (AwsSnsPushChannel.APNS.equals(awsSnsEndpoint.getPushChannel())) {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("APNS", getSampleAppleMessage(message, data));
                    String msg = jsonify(messageMap);
                    log.info("APNS: [{}]", msg);
                    publishRequest = PublishRequest.builder()
                            .targetArn(awsSnsEndpoint.getEndpointArn())
                            .messageStructure("json")
                            .message(msg)
                            .build();
                } else if (AwsSnsPushChannel.FCM.equals(awsSnsEndpoint.getPushChannel())) {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("GCM", getSampleAndroidMessage(message, data));
                    String msg = jsonify(messageMap);
                    log.info("GCM: [{}]", msg);
                    publishRequest = PublishRequest.builder()
                            .targetArn(awsSnsEndpoint.getEndpointArn())
                            .messageStructure("json")
                            .message(msg)
                            .build();
                } else {
                    publishRequest = PublishRequest.builder()
                            .targetArn(awsSnsEndpoint.getEndpointArn())
                            .message(message)
                            .build();
                }

                PublishResponse publishResponse = snsClient.publish(publishRequest);
                log.info("publish message success messageId:[{}] sequenceNumber:[{}] memberId:[{}] message:[{}]",
                        publishResponse.messageId(), publishResponse.sequenceNumber(), awsSnsEndpointList, message);

            } catch (SnsException e) {
                log.error("publish awsSnsEndpointList:[{}] message:[{}]", awsSnsEndpointList, message, e);
            }
        }
    }

    public void publishGlobalNotify(String message) {
        try {
            AwsSnsProperties.TopicArn topic = awsSnsProperties.getTopic();
            String globalNotificationArn = topic.getGlobalNotification();

            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(globalNotificationArn)
                    .build();

            PublishResponse result = snsClient.publish(request);
            log.info("publishGlobalNotify statusCode:[{}] statusText:[{}] messageId:[{}]",
                    result.sdkHttpResponse().statusCode(), result.sdkHttpResponse().statusText(), result.messageId());

        } catch (SnsException e) {
            log.error("publishGlobalNotify message:[{}]", message, e);
            throw CommonError.UNEXPECT_ERROR.withDefaults(e);
        }
    }

    public List<AwsSnsEndpoint> getAwsSnsEndpointList(Collection<Long> memberIds) {
        if (CollectionUtils.isEmpty(memberIds)) {
            return Collections.emptyList();
        }
        return awsSnsEndpointMapper.selectList(Wrappers.lambdaQuery(AwsSnsEndpoint.class)
                .in(AwsSnsEndpoint::getMemberId, memberIds));
    }

    public String getSampleAppleMessage(String message, Map<String, Object> data) {
        Map<String, Object> appleMessageMap = new HashMap<>();
        Map<String, Object> appMessageMap = new HashMap<>();
        appMessageMap.put("alert", message);
        appMessageMap.putAll(data);
        appleMessageMap.put("aps", appMessageMap);
        return jsonify(appleMessageMap);
    }

    public String getSampleAndroidMessage(String message, Map<String, Object> data) {
        Map<String, Object> appleMessageMap = new HashMap<>();
        Map<String, Object> appMessageMap = new HashMap<>();
        appMessageMap.put("message", message);
        appMessageMap.putAll(data);
        appleMessageMap.put("data", appMessageMap);
        return jsonify(appleMessageMap);
    }

    public String jsonify(Object message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw (RuntimeException) e;
        }
    }
}
