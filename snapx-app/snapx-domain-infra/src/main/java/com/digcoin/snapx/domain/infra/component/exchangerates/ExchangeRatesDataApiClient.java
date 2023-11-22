package com.digcoin.snapx.domain.infra.component.exchangerates;

import com.digcoin.snapx.domain.infra.component.exchangerates.model.LatestReq;
import com.digcoin.snapx.domain.infra.component.exchangerates.model.LatestResp;
import com.digcoin.snapx.domain.infra.component.exchangerates.model.SymbolsResp;
import com.digcoin.snapx.domain.infra.config.properties.ExchangeRatesDataApiProperties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/5/10 11:44
 * @description
 * @documentation https://apilayer.com/marketplace/exchangerates_data-api
 */
@Slf4j
@AllArgsConstructor
@Component
public class ExchangeRatesDataApiClient {

    private final ExchangeRatesDataApiProperties exchangeRatesDataApiProperties;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://api.apilayer.com/exchangerates_data";

    @SneakyThrows
    public LatestResp latest(LatestReq req) {
        String url = getUrl("latest");
        LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("base", req.getBase());
        queryParams.add("symbols", req.getSymbols());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.queryParams(queryParams).build().encode().toUri();

        HttpEntity<Object> entity = new HttpEntity<>(queryParams, getHttpHeaders());
        return restTemplate.exchange(uri, HttpMethod.GET, entity, LatestResp.class).getBody();
    }

    public SymbolsResp symbols() {
        HttpEntity<Object> entity = new HttpEntity<>(null, getHttpHeaders());
        return restTemplate.exchange(getUrl("symbols"), HttpMethod.GET, entity, SymbolsResp.class).getBody();
    }

    private String getUrl(String uri) {
        return String.format("%s/%s", BASE_URL, uri);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("apikey", exchangeRatesDataApiProperties.getApiKey());
        return headers;
    }

}
