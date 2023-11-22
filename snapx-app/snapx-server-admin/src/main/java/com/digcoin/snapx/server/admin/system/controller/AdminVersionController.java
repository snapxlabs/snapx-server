package com.digcoin.snapx.server.admin.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.server.admin.system.dto.AdminVersionDTO;
import com.digcoin.snapx.server.admin.system.dto.AdminVersionQueryDTO;
import com.digcoin.snapx.server.admin.system.service.AdminVersionService;
import com.digcoin.snapx.server.admin.system.vo.AdminVersionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/13 20:06
 * @description
 */
@Tag(name = "APP版本管理")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/system/version")
@RequiredArgsConstructor
public class AdminVersionController {

    private final AdminVersionService adminVersionService;

    @Operation(summary = "创建APP版本更新")
    @PostMapping("/create")
    public void createVersion(@Valid @RequestBody AdminVersionDTO dto) {
        adminVersionService.createVersion(dto);
    }

    @Operation(summary = "APP版本更新")
    @GetMapping("/page")
    public PageResult<AdminVersionVO> pageVersion(AdminVersionQueryDTO dto) {
        return adminVersionService.pageVersion(dto);
    }

    @Operation(summary = "获取单个活动详情")
    @GetMapping("/{id}")
    public AdminVersionVO getVersion(@Valid @NotNull(message = "Id cannot be null")
                                       @Schema(description = "版本活动Id", required = true)
                                       @PathVariable("id") Long id) {
        return adminVersionService.getVersion(id);
    }

    @Operation(summary = "删除活动")
    @PostMapping("/delete/{id}")
    public void deleteVersion(@Valid @NotNull(message = "Id cannot be null")
                               @Schema(description = "版本活动Id", required = true)
                               @PathVariable("id") Long id) {
        adminVersionService.deleteVersion(id);
    }

    @Operation(summary = "修改活动")
    @PostMapping("/edit/{id}")
    public void editVersion(@Valid @NotNull(message = "Id cannot be null")
                             @Schema(description = "版本活动Id", required = true)
                             @PathVariable("id") Long id, @Validated @RequestBody AdminVersionDTO dto) {
        adminVersionService.editVersion(id, dto);
    }

}
