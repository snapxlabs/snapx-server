package com.digcoin.snapx.server.base.infra.service;

import com.digcoin.snapx.domain.infra.component.exchangerates.ExchangeRatesDataApiClient;
import com.digcoin.snapx.domain.infra.component.exchangerates.Symbols;
import com.digcoin.snapx.domain.infra.component.exchangerates.model.LatestReq;
import com.digcoin.snapx.domain.infra.component.exchangerates.model.LatestResp;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/5/10 13:37
 * @description
 */
@Slf4j
@AllArgsConstructor
@Service
public class BaseExchangeRatesService {

    private final static String KEY_PREFIX = "SNAPX:EXCHANGERATES";
    private final static String KEY_SYNC_INFO = KEY_PREFIX + ":SYNC_INFO";
    private final static String KEY_MAP = KEY_PREFIX + ":MAP";

    private final ExchangeRatesDataApiClient exchangeRatesDataApiClient;
    private final RedisTemplate<String, Object> redisTemplate;

    public void syncLatest() {
        log.info("syncLatest");
        List<Symbols> symbols = Arrays.asList(Symbols.USD, Symbols.HKD);
        // String symbolsStr = Joiner.on(",").join(symbols.stream().map(Symbols::getSymbol).collect(Collectors.toList()));
        LatestResp resp = exchangeRatesDataApiClient.latest(new LatestReq(Symbols.USD.getSymbol(), ""));
        if (Objects.isNull(resp) || !Objects.equals(resp.getSuccess(), true)) {
            log.info("syncLatest error");
            return;
        }

        LatestResp.RatesDTO rates = resp.getRates();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Double> map = objectMapper.convertValue(rates, Map.class);
        redisTemplate.opsForValue().set(KEY_SYNC_INFO, resp);
        redisTemplate.opsForHash().putAll(KEY_MAP, map);
    }

    public BigDecimal getPriceBySymbol(Symbols symbol) {
        Double rate = (Double) redisTemplate.opsForHash().get(KEY_MAP, symbol);
        return new BigDecimal(rate);
    }

    public BigDecimal getUsdExchangeRateBySymbol(Symbols symbol) {
        if (Objects.isNull(symbol)) {
            return null;
        }
        BigDecimal usdPrice = getPriceBySymbol(Symbols.USD);
        BigDecimal symbolPrice = getPriceBySymbol(symbol);
        BigDecimal exchangeRate = usdPrice.divide(symbolPrice, 4, RoundingMode.HALF_UP);
        return exchangeRate;
    }

}
