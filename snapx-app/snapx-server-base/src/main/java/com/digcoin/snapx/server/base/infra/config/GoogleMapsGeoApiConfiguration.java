package com.digcoin.snapx.server.base.infra.config;

import com.digcoin.snapx.domain.infra.component.googlemap.GeoApiContextFactory;
import com.digcoin.snapx.domain.infra.config.properties.GoogleMapsGeoProperties;
import com.digcoin.snapx.domain.infra.config.properties.VpnProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:33
 * @description
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class GoogleMapsGeoApiConfiguration {

    private final GoogleMapsGeoProperties googleMapsGeoProperties;
    private final VpnProperties vpnProperties;
    private final ObjectMapper objectMapper;

    @Bean
    public GeoApiContextFactory geoApiContextFactory() {
        GeoApiContextFactory factory = new GeoApiContextFactory();
        factory.setGoogleMapsGeoProperties(googleMapsGeoProperties);
        factory.setVpnProperties(vpnProperties);
        factory.setObjectMapper(objectMapper);
        return factory;
    }

}