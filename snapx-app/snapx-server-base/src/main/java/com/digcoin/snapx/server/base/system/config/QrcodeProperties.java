package com.digcoin.snapx.server.base.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.qrcode")
public class QrcodeProperties {

    private CodeDescription invite;

    @Data
    public static class CodeDescription {

        private String url;

        private Integer width;

        private String imageType;

    }
}
