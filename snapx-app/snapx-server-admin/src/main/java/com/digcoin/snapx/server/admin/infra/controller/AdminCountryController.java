package com.digcoin.snapx.server.admin.infra.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.server.base.infra.dto.CountryDTO;
import com.digcoin.snapx.server.base.infra.service.BaseCountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/5 15:32
 * @description
 */
@Tag(name = "230405 - 国家")
@Slf4j
@SaCheckLogin
@Validated
@RestController
@RequestMapping("/infra/country")
@RequiredArgsConstructor
public class AdminCountryController {

    private final BaseCountryService baseCountryService;

    @Operation(summary = "获取国家列表")
    @GetMapping("list-countries")
    public List<CountryDTO> listCountries() {
        return baseCountryService.listCountries();
    }

}
