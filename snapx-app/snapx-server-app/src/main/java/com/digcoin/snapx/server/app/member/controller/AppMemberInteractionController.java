package com.digcoin.snapx.server.app.member.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.server.app.member.dto.AppMemberInteractionDTO;
import com.digcoin.snapx.server.app.member.service.AppMemberInteractionService;
import com.digcoin.snapx.server.app.member.vo.AppMemberInteractionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 10:57
 * @description
 */
@Tag(name = "103 - 会员 - 互动信息")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/member-interaction")
@RequiredArgsConstructor
public class AppMemberInteractionController {

    private final AppMemberInteractionService appMemberInteractionService;

    @Operation(summary = "获取当前会员的互动信息")
    @GetMapping("/get")
    public AppMemberInteractionVO getMemberInteraction(@Schema(hidden = true) CurrentUser currentUser) {
        return appMemberInteractionService.getMemberInteraction(currentUser.getId());
    }

    @Operation(summary = "更新会员互动信息")
    @PostMapping("/edit")
    public AppMemberInteractionVO editMemberInteraction(@Schema(hidden = true) CurrentUser currentUser,
                                                        @Validated @RequestBody AppMemberInteractionDTO dto) {
        dto.setMemberId(currentUser.getId());
        return appMemberInteractionService.editMemberInteraction(dto);
    }

}
