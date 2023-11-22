package com.digcoin.snapx.server.admin.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.server.base.system.dto.AdminSystemSettingDTO;
import com.digcoin.snapx.server.admin.system.service.AdminSystemSettingService;
import com.digcoin.snapx.server.base.system.vo.AdminSystemSettingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 15:54
 * @description
 */
@Tag(name = "系统配置管理")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/system/setting")
@RequiredArgsConstructor
public class AdminSystemSettingController {

    private final AdminSystemSettingService adminSystemSettingService;

    @Operation(summary = "获取系统配置")
    @GetMapping("/get")
    public AdminSystemSettingVO getSystemSetting() {
       return adminSystemSettingService.getSystemSetting();
    }

    @Operation(summary = "更新系统配置")
    @PostMapping("/edit")
    public void editSystemSetting(@Validated @RequestBody AdminSystemSettingDTO dto) {
        adminSystemSettingService.editSystemSetting(dto);
    }

}
