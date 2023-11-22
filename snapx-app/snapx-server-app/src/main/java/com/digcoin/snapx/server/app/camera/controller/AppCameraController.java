package com.digcoin.snapx.server.app.camera.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.server.app.camera.dto.AppCameraQueryDTO;
import com.digcoin.snapx.server.app.camera.service.AppCameraService;
import com.digcoin.snapx.server.app.camera.vo.AppCameraVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 15:33
 * @description
 */
@Tag(name = "601 - 相机 - 相机信息")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/camera")
@RequiredArgsConstructor
public class AppCameraController {

    private final AppCameraService appCameraService;

    @Operation(summary = "获取相机列表")
    @GetMapping("/list")
    public List<AppCameraVO> listCamera(AppCameraQueryDTO dto) {
        return appCameraService.listCamera(dto);
    }

    @Operation(summary = "获取单个相机详情")
    @GetMapping("/{id}")
    public AppCameraVO getCamera(@Validated @NotNull(message = "camera id cannot be null ")
                                      @Schema(description = "相机ID") @PathVariable("id") Long id) {
        return appCameraService.getCamera(id);
    }


}
