package com.digcoin.snapx.domain.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties("app.push.aws")
public class AwsSnsProperties {

    private TopicArn topic;

    private Map<String, PlatformApp> platformApplicationArn;

    @Data
    public static class PlatformApp {
        private String name;
        private String pushChannel;
        private String arn;
    }

    @Data
    public static class TopicArn {

        private String globalNotification;

    }
}
