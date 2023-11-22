package com.digcoin.snapx.server.admin.camera.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.server.admin.camera.dto.AdminCameraMemberPageDTO;
import com.digcoin.snapx.server.admin.camera.service.AdminCameraMemberService;
import com.digcoin.snapx.server.admin.camera.vo.AdminCameraMemberVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/22 1:25
 * @description
 */
@Tag(name = "602 - 会员相机 - 会员相机")
@Slf4j
@SaCheckLogin
@Validated
@RestController
@RequestMapping("/camera-member")
@RequiredArgsConstructor
public class AdminCameraMemberController {

    private final AdminCameraMemberService adminMemberCameraService;

    @Operation(summary = "会员相机分页")
    @GetMapping("/page")
    public PageResult<AdminCameraMemberVO> pageCameraMember(AdminCameraMemberPageDTO dto) {
        return adminMemberCameraService.pageCameraMember(dto);
    }



}
