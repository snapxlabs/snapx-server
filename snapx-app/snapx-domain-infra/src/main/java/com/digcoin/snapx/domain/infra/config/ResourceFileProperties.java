package com.digcoin.snapx.domain.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app.resource-file")
public class ResourceFileProperties {

    private String strategy;

    private String resourceUrl;

    private String path;

    private String location;

    private AwsS3 s3;

    @Data
    public static class AwsS3 {

        private String cdnDomain;

        private String bucket;

    }
}
