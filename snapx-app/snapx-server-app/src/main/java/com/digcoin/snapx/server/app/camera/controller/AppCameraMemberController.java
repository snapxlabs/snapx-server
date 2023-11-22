package com.digcoin.snapx.server.app.camera.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.server.app.camera.service.AppCameraMemberService;
import com.digcoin.snapx.server.app.camera.vo.AppCameraMemberVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 17:21
 * @description
 */
@Tag(name = "602 - 相机 - 会员相机信息")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/camera-member")
@RequiredArgsConstructor
public class AppCameraMemberController {

    private final AppCameraMemberService appCameraMemberService;

    @Operation(summary = "获取当前会员的相机列表")
    @GetMapping("/list")
    public List<AppCameraMemberVO> listCameraMember(@Schema(hidden = true) CurrentUser currentUser) {
        return appCameraMemberService.listCameraMember(currentUser.getId());
    }

}
