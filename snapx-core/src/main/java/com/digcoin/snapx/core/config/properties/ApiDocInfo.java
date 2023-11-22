package com.digcoin.snapx.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.docs")
public class ApiDocInfo {

    private String title;
    private String description;
    private String version;
    private String appName;
    private String appUrl;
}
