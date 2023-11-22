package com.digcoin.snapx.server.app.member.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.Pageable;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.server.app.member.dto.AppMemberActivityDTO;
import com.digcoin.snapx.server.app.member.service.AppMemberActivityService;
import com.digcoin.snapx.server.app.member.vo.AppMemberActivityVO;
import com.digcoin.snapx.server.base.member.dto.BaseTotalGiftCountDTO;
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
 * @date 2023/2/13 11:52
 * @description
 */
@Tag(name = "105 - 会员活动")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/member-activity")
@RequiredArgsConstructor
public class AppMemberActivityController {

    private final AppMemberActivityService appMemberActivityService;

    @Operation(summary = "保存会员参与活动记录，并赠送usdc、积分等")
    @PostMapping("/take-part-in")
    public AppMemberActivityVO takePartInActivity(@Schema(hidden = true) CurrentUser currentUser,
                                   @Validated @RequestBody AppMemberActivityDTO dto) {
        dto.setMemberId(currentUser.getId());
        return appMemberActivityService.takePartInActivity(dto);
    }

    @Operation(summary = "获取会员参加单个活动记录及赠送详情")
    @GetMapping("/get")
    public AppMemberActivityVO getMemberActivity(@Schema(hidden = true) CurrentUser currentUser,
                                                 @Validated @Schema(description = "活动Id")
                                                 @NotNull(message = "Activity id cannot be null")
                                                 @RequestParam(value = "activityId") Long activityId) {
        return appMemberActivityService.getMemberActivity(currentUser.getId(), activityId);
    }

    @Operation(summary = "获取会员参加活动记录及赠送分页")
    @GetMapping("/page")
    public PageResult<AppMemberActivityVO> pageMemberActivity(@Schema(hidden = true) CurrentUser currentUser,
                                                              Pageable pageable) {
        return appMemberActivityService.pageMemberActivity(currentUser.getId(), pageable);
    }

    @Operation(summary = "获取所有参与活动已获取总数量")
    @GetMapping("/gift-total")
    public BaseTotalGiftCountDTO getActivityGiftTotal(@Schema(hidden = true) CurrentUser currentUser) {
        return appMemberActivityService.getActivityGiftTotal(currentUser.getId());
    }

}
