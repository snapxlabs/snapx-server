package com.digcoin.snapx.server.app.infra.controller;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.infra.bo.PhoneAreaCodeQuery;
import com.digcoin.snapx.domain.infra.entity.PhoneAreaCode;
import com.digcoin.snapx.domain.infra.service.PhoneAreaCodeService;
import com.digcoin.snapx.server.app.infra.converter.PhoneAreaCodeConverter;
import com.digcoin.snapx.server.app.infra.dto.PhoneAreaCodeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "344 - 电话地区代码")
@RequiredArgsConstructor
@RequestMapping("/infra/phone-area-code")
@RestController
public class PhoneAreaCodeController {

    private final PhoneAreaCodeService phoneAreaCodeService;
    private final PhoneAreaCodeConverter phoneAreaCodeConverter;

    @GetMapping("page-phone-area-code")
    @Operation(description = "分页获取电话地区代码列表")
    public PageResult<PhoneAreaCodeDTO> pagePhoneAreaCode(PhoneAreaCodeQuery query) {
        return PageResult.fromPageResult(phoneAreaCodeService.pagePhoneAreaCode(query), phoneAreaCodeConverter::fromEntity);
    }

    @GetMapping("get-phone-area-code")
    @Operation(description = "根据国家地区代码获取电话地区代码")
    public PhoneAreaCodeDTO getPhoneAreaCode(@RequestParam String countryCode) {
        return phoneAreaCodeConverter.fromEntity(phoneAreaCodeService.getPhoneAreaCode(countryCode));
    }

}
