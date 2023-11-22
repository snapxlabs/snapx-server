package com.digcoin.snapx.server.base;

import com.digcoin.snapx.domain.infra.config.properties.GoogleMapsGeoProperties;
import com.digcoin.snapx.domain.infra.config.properties.VpnProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
        GoogleMapsGeoProperties.class,
        VpnProperties.class,
})
@Configuration
@ComponentScan({
        "com.digcoin.snapx.server.base.**.converter",
        "com.digcoin.snapx.server.base.**.component",
        "com.digcoin.snapx.server.base.**.manager",
        "com.digcoin.snapx.server.base.**.service",
        "com.digcoin.snapx.server.base.**.assembler",
        "com.digcoin.snapx.server.base.**.config"
})
public class SnapxServerBaseAutoConfiguration {
}
