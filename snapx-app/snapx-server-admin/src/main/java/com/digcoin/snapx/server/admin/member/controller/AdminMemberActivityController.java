package com.digcoin.snapx.server.admin.member.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.server.admin.member.dto.AdminMemberActivityPageDTO;
import com.digcoin.snapx.server.admin.member.service.AdminMemberActivityService;
import com.digcoin.snapx.server.admin.member.vo.AdminMemberActivityVO;
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
 * @date 2023/4/6 14:31
 * @description
 */
@Tag(name = "101 - 会员活动")
@Slf4j
@SaCheckLogin
@Validated
@RestController
@RequestMapping("/member-activity")
@RequiredArgsConstructor
public class AdminMemberActivityController {

    private final AdminMemberActivityService adminMemberActivityService;

    @Operation(summary = "会员相机分页")
    @GetMapping("/page")
    public PageResult<AdminMemberActivityVO> pageMemberActivity(AdminMemberActivityPageDTO dto) {
        return adminMemberActivityService.pageMemberActivity(dto);
    }
}
