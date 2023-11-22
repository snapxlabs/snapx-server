package com.digcoin.snapx.domain.infra;

import com.digcoin.snapx.domain.infra.config.AwsSnsProperties;
import com.digcoin.snapx.domain.infra.config.ResourceFileProperties;
import com.digcoin.snapx.domain.infra.config.ResourceLocationConfig;
import com.digcoin.snapx.domain.infra.config.properties.DiscordOauthProperties;
import com.digcoin.snapx.domain.infra.config.properties.ExchangeRatesDataApiProperties;
import com.digcoin.snapx.domain.infra.config.properties.GoogleMapsGeoProperties;
import com.digcoin.snapx.domain.infra.config.properties.VpnProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({ResourceLocationConfig.class})
@Configuration
@EnableConfigurationProperties({
        ResourceFileProperties.class,
        AwsSnsProperties.class,
        GoogleMapsGeoProperties.class,
        DiscordOauthProperties.class,
        ExchangeRatesDataApiProperties.class,
        VpnProperties.class
})
@ComponentScan("com.digcoin.snapx.domain.infra.config")
@ComponentScan("com.digcoin.snapx.domain.infra.component")
@ComponentScan("com.digcoin.snapx.domain.infra.service")
@MapperScan("com.digcoin.snapx.domain.infra.mapper")
public class SnapxInfraDomainAutoConfiguration {
}
