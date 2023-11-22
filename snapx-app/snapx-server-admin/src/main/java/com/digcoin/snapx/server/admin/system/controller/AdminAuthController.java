package com.digcoin.snapx.server.admin.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.server.admin.system.dto.AdminUserAuthDTO;
import com.digcoin.snapx.server.admin.system.dto.AdminUserDTO;
import com.digcoin.snapx.server.admin.system.service.AdminAuthAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "系统管理员鉴权")
@Slf4j
@RestController
@RequestMapping("/system/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthAppService adminAuthAppService;

    @Operation(description = "系统管理员用户名密码登录")
    @PostMapping("/login")
    public AdminUserAuthDTO login(@Schema(description = "用户名") @RequestParam String username,
                                  @Schema(description = "密码", type = "string", format = "password") @RequestParam byte[] password) {
        return adminAuthAppService.login(username, password);
    }

    @SaCheckLogin
    @Operation(description = "获取当前用户信息")
    @GetMapping("/find-current-user-info")
    public AdminUserDTO findCurrentUserInfo(@Schema(hidden = true) CurrentUser currentUser) {
        return adminAuthAppService.findCurrentUserInfo(currentUser.getId());
    }

    @Operation(description = "系统管理员登出")
    @PostMapping("/logout")
    public void logout() {
        adminAuthAppService.logout();
    }

}
