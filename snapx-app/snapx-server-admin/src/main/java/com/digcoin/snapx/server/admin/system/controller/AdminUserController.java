package com.digcoin.snapx.server.admin.system.controller;

import com.digcoin.snapx.domain.system.bo.AdminUserQuery;
import com.digcoin.snapx.server.admin.system.service.AdminUserAppService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.server.admin.system.dto.AdminUserDTO;
import com.digcoin.snapx.core.mybatis.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "系统管理员管理")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/system/admin-user")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserAppService adminUserAppService;

    @Operation(summary = "创建系统管理员")
    @PostMapping("create-admin-user")
    public AdminUserDTO createAdminUser(@Validated @RequestBody AdminUserDTO adminUser) {
        return adminUserAppService.createAdminUser(adminUser);
    }

    @Operation(summary = "删除系统管理员")
    @PostMapping("delete-admin-user/{id}")
    public AdminUserDTO deleteAdminUser(@Schema(description = "管理员id", type = "string") @PathVariable Long id) {
        return adminUserAppService.deleteAdminUser(id);
    }

    @Operation(summary = "更新系统管理员")
    @PostMapping("update-admin-user/{id}")
    public AdminUserDTO updateAdminUser(@Schema(description = "管理员id", type = "string") @PathVariable Long id,
                                        @Validated @RequestBody AdminUserDTO adminUser) {
        return adminUserAppService.updateAdminUser(id, adminUser);
    }

    @Operation(summary = "获取管理员详情")
    @GetMapping("find-admin-user/{id}")
    public AdminUserDTO findAdminUser(@Schema(description = "管理员id", type = "string") @PathVariable Long id) {
        return adminUserAppService.findAdminUser(id);
    }

    @Operation(summary = "分页查询管理员列表")
    @GetMapping("page-admin-user")
    public PageResult<AdminUserDTO> pageAdminUser(AdminUserQuery query) {
        return adminUserAppService.pageAdminUser(query);
    }

}
