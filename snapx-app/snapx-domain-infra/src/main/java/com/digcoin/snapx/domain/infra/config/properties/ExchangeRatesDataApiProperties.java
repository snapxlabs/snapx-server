package com.digcoin.snapx.domain.infra.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/5/10 11:44
 * @description
 */
@ConfigurationProperties("app.exchange-rates-data-api")
@Data
public class ExchangeRatesDataApiProperties {

    private String apiKey;

}
