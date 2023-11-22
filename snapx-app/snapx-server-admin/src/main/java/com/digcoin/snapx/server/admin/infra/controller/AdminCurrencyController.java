package com.digcoin.snapx.server.admin.infra.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.domain.infra.entity.Currency;
import com.digcoin.snapx.domain.infra.service.CurrencyService;
import com.digcoin.snapx.server.base.infra.converter.BaseCurrencyConverter;
import com.digcoin.snapx.server.base.infra.dto.CurrencyDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/5/3 22:14
 * @description
 */
@Tag(name = "230503 - 货币")
@Slf4j
@SaCheckLogin
@RequiredArgsConstructor
@RestController
@RequestMapping("/infra/currency")
public class AdminCurrencyController {

    private final CurrencyService currencyService;
    private final BaseCurrencyConverter baseCurrencyConverter;

    @Operation(summary = "获取货币列表")
    @PostMapping("list-currencies")
    public List<CurrencyDTO> listCurrencies() {
        List<Currency> currencies = currencyService.listCurrencies();
        return baseCurrencyConverter.toDTO(currencies);
    }

}
