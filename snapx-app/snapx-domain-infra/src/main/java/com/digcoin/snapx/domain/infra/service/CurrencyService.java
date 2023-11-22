package com.digcoin.snapx.domain.infra.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.domain.infra.entity.Currency;
import com.digcoin.snapx.domain.infra.mapper.CurrencyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/3 22:27
 * @description
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CurrencyService {

    private final CurrencyMapper currencyMapper;

    public List<Currency> listCurrencies() {
        return currencyMapper.selectList(Wrappers.<Currency>lambdaQuery().orderByAsc(Currency::getCreateBy));
    }

    public List<Currency> listByCodes(List<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        return currencyMapper.selectList(Wrappers.<Currency>lambdaQuery()
                .in(Currency::getCode, codes)
                .orderByAsc(Currency::getCreateBy)
        );
    }

}
