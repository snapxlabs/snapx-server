package com.digcoin.snapx.domain.infra.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/16 09:26
 * @description
 */
@ConfigurationProperties("google-maps.geo")
@Data
public class GoogleMapsGeoProperties {

    private String apiKey;

}
