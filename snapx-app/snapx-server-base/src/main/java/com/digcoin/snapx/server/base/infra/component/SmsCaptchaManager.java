package com.digcoin.snapx.server.base.infra.component;

import com.digcoin.snapx.core.common.error.CommonError;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.pinpoint.model.*;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.*;

@Slf4j
public class SmsCaptchaManager extends AbstractCaptchaManager implements CaptchaManager {

    public SmsCaptchaManager(CaptchaManagerFactory captchaManagerFactory) {
        super(captchaManagerFactory);
    }

    @Override
    protected CaptchaChannel getCaptchaChannel() {
        return CaptchaChannel.SMS;
    }

    @Override
    protected void doSend(CaptchaType type, String receiver, String code) {

        doSendUseSnsClient(type, receiver, code);

        // 之前以为发送中国大陆必须使用固定消息模板，而固定消息模板只能用PinpointClient发送。
        // 但是出现了新加坡，菲律宾收不到短信的情况
        // 且用PinpointClient发送CloudWatch中不产生日志，无法排查。
        // 现在好像中国大陆消息内容备案过后，不用固定模板也能发送，所以统一使用原来的SnsClient发送
//        doSendUsePinpointClient(type, receiver, code);

        log.info("send SMS phone:[{}] code:[{}]", receiver, code);
    }

    protected void doSendUseSnsClient(CaptchaType type, String receiver, String code) {
        Map<String, MessageAttributeValue> attributes = new HashMap<>(1);
        attributes.put("AWS.SNS.SMS.SMSType", MessageAttributeValue.builder()
                .dataType("String")
                .stringValue("Transactional")
                .build());
        SnsClient snsClient = captchaManagerFactory.getSnsClient();
        PublishRequest request = PublishRequest.builder()
                .messageAttributes(attributes)
                .phoneNumber("+"+receiver)
                .message(String.format("[SnapX] S-%s is your SnapX verification code. This code will expire in %d minutes.", code, 30))
                .build();
        PublishResponse response = snsClient.publish(request);
        log.info("send SNS message success statusCode:[{}] messageId:[{}] sequenceNumber:[{}] receiver:[{}] code:[{}] responseMetadata:[{}]",
                response.sdkHttpResponse().statusCode(), response.messageId(), response.sequenceNumber(), receiver, code, response.responseMetadata());
    }

    private void doSendUsePinpointClient(CaptchaType type, String receiver, String code) {
        String phone = "+" + receiver;
        Map<String, Collection<String>> templateData = Map.of("captcha", Set.of(code), "minutes", Set.of("30"));
        Map<String, AddressConfiguration> addressMap = new HashMap<>();
        AddressConfiguration addConfig = AddressConfiguration.builder()
                .channelType(ChannelType.SMS)
                .substitutions(templateData)
                .build();

        addressMap.put(phone, addConfig);
        SMSMessage smsMessage = SMSMessage.builder()
                .body("")
                .messageType("TRANSACTIONAL")
                .build();

        DirectMessageConfiguration direct = DirectMessageConfiguration.builder()
                .smsMessage(smsMessage)
                .build();

        TemplateConfiguration template = TemplateConfiguration.builder()
                .smsTemplate(Template.builder()
                        .name("snapx-bind-phone")
                        .build())
                .build();

        MessageRequest msgReq = MessageRequest.builder()
                .addresses(addressMap)
                .messageConfiguration(direct)
                .templateConfiguration(template)
                .build();

        SendMessagesRequest request = SendMessagesRequest.builder()
                .applicationId("237321f44f4448d7a725cd50e8c21a0c")
                .messageRequest(msgReq)
                .build();

        SendMessagesResponse response= captchaManagerFactory.getPinpointClient().sendMessages(request);
        MessageResponse messageResponse = response.messageResponse();
        Map<String, MessageResult> result = messageResponse.result();

        //Write out the result of sendMessage.
        result.forEach((k, v) -> System.out.println((k + ":" + v)));

        MessageResult messageResult = result.get(phone);

        if (!Objects.equals(200, messageResult.statusCode())) {
            log.error("send SMS fail phone:[{}] DeliveryStatus:[{}] MessageId:[{}] statusCode:[{}] statusMessage:[{}]",
                    phone, messageResult.deliveryStatus(), messageResult.messageId(), messageResult.statusCode(), messageResult.statusMessage());
            throw CommonError.UNEXPECT_ERROR.withMessage(messageResult.statusMessage());
        }
    }

}
