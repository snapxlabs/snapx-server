package com.digcoin.snapx.server.app.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.digcoin.snapx.server.app.system.service.SystemSettingAppService;
import com.digcoin.snapx.server.base.system.vo.AdminSystemSettingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "800 - 系统 - 系统配置")
@Slf4j
@Validated
@RestController
@RequestMapping("/system/setting")
@RequiredArgsConstructor
public class SystemSettingController {

    private final SystemSettingAppService systemSettingAppService;

    @SaIgnore
    @Operation(summary = "获取系统设置参数")
    @GetMapping("/get-system-setting")
    public AdminSystemSettingVO getSystemSetting() {
        return systemSettingAppService.getSystemSetting();
    }

}
