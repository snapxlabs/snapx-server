package com.digcoin.snapx.domain.infra.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/6 17:49
 * @description
 */
@ConfigurationProperties("app.vpn")
@Data
public class VpnProperties {

    private Boolean enabled;
    private String hostname;
    private Integer port;

}
