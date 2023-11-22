package com.digcoin.snapx.server.admin.camera.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.server.admin.camera.dto.AdminCameraDTO;
import com.digcoin.snapx.server.admin.camera.dto.AdminCameraPageDTO;
import com.digcoin.snapx.server.admin.camera.service.AdminCameraService;
import com.digcoin.snapx.server.admin.camera.vo.AdminCameraVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 16:25
 * @description
 */
@Tag(name = "601 - 相机 - 相机管理")
@Slf4j
@SaCheckLogin
@Validated
@RestController
@RequestMapping("/camera")
@RequiredArgsConstructor
public class AdminCameraController {

    private final AdminCameraService adminCameraService;

    @Operation(summary = "创建相机")
    @PostMapping("/create")
    public void createCamera(@Validated @RequestBody AdminCameraDTO dto) {
        adminCameraService.createCamera(dto);
    }

    @Operation(summary = "相机分页")
    @GetMapping("/page")
    public PageResult<AdminCameraVO> pageCamera(AdminCameraPageDTO dto) {
        return adminCameraService.pageCamera(dto);
    }

    @Operation(summary = "获取相机详情")
    @GetMapping("/get/{id}")
    public AdminCameraVO getCamera(@Validated @NotNull(message = "相机Id不能为空")
                                   @Schema(description = "相机Id", required = true)
                                   @PathVariable("id") Long id) {
        return adminCameraService.getCamera(id);
    }

    @Operation(summary = "编辑相机")
    @PostMapping("/edit/{id}")
    public void editCamera(@Validated @NotNull(message = "相机Id不能为空")
                           @Schema(description = "相机Id", required = true)
                           @PathVariable("id") Long id,
                           @Validated @RequestBody AdminCameraDTO dto) {
        adminCameraService.editCamera(id, dto);
    }

    @Operation(summary = "删除相机")
    @PostMapping("/delete/{id}")
    public void deleteCamera(@Validated @NotNull(message = "相机Id不能为空")
                             @Schema(description = "相机Id", required = true)
                             @PathVariable("id") Long id) {
        adminCameraService.deleteCamera(id);
    }

}
