package com.digcoin.snapx.server.admin.member.controller;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.member.bo.InviteeMemberQuery;
import com.digcoin.snapx.domain.member.bo.InviterCommissionQuery;
import com.digcoin.snapx.domain.member.bo.MemberEventQuery;
import com.digcoin.snapx.domain.member.bo.MemberQuery;
import com.digcoin.snapx.server.admin.member.dto.*;
import com.digcoin.snapx.server.admin.member.service.MemberAppService;
import com.digcoin.snapx.server.base.member.service.MemberRankingService;
import com.digcoin.snapx.server.base.member.dto.InviterCommissionDTO;
import com.digcoin.snapx.server.base.member.service.InviterCommissionAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "100 - 会员管理")
@Slf4j
@Validated
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberAppService memberAppService;
    private final MemberRankingService memberRankingService;
    private final InviterCommissionAppService inviterCommissionAppService;

    @Operation(summary = "获取用户统计数据")
    @GetMapping("/get-member-statistics")
    public MemberEventStatisticsDTO getMemberStatistics(@RequestParam(required = false) LocalDateTime currentTime) {
        return memberAppService.getMemberStatistics(Optional.ofNullable(currentTime).orElse(LocalDateTime.now()));
    }

    @Operation(summary = "分页获取用户访问事件（按月聚合）")
    @GetMapping("/page-event-per-month")
    public PageResult<MemberEventDTO> pageEventPerMonth(MemberEventQuery query) {
        return memberAppService.pageEventPerMonth(query);
    }

    @Operation(summary = "分页获取用户访问事件（按天聚合）")
    @GetMapping("/page-event-per-day")
    public PageResult<MemberEventDTO> pageEventPerDay(MemberEventQuery query) {
        return memberAppService.pageEventPerDay(query);
    }

    @Operation(summary = "分页获取用户访问事件（按小时聚合）")
    @GetMapping("/page-event-per-hour")
    public PageResult<MemberEventDTO> pageEventPerHour(MemberEventQuery query) {
        return memberAppService.pageEventPerHour(query);
    }

    @Operation(summary = "分页获取会员列表")
    @GetMapping("/page-member")
    public PageResult<MemberDTO> pageMember(MemberQuery query) {
        if (StringUtils.isNotBlank(query.getMemberIdList())) {
            query.setMemberIds(Arrays.stream(query.getMemberIdList().split(",")).map(Long::valueOf).collect(Collectors.toSet()));
        }
        return memberAppService.pageMember(query);
    }

    @Operation(summary = "分页获取被邀请会员列表")
    @GetMapping("/page-invitee-member")
    public PageResult<InviteeMemberDTO> pageMember(InviteeMemberQuery query) {
        return memberAppService.pageInviteeMember(query);
    }

    @Operation(summary = "获取会员详情")
    @GetMapping("/find-member/{id}")
    public MemberDTO findMember(@PathVariable Long id) {
        return memberAppService.findMember(id);
    }

    @Operation(summary = "更新会员冻结状态")
    @PostMapping("update-member-freeze/{id}")
    public MemberDTO updateMemberFreeze(@Schema(description = "会员id") @PathVariable Long id, @RequestBody MemberFreezeDTO memberFreeze) {
        memberAppService.updateMemberFreeze(id, memberFreeze);
        return memberAppService.findMember(id);
    }

    @Operation(summary = "创建会员")
    @PostMapping("create-member")
    public MemberDTO createMember(@RequestBody MemberDTO member) {
        Long id = memberAppService.createMember(member);
        return findMember(id);
    }

    @Operation(summary = "更新会员信息")
    @PostMapping("update-member/{id}")
    public MemberDTO updateMember(@PathVariable Long id, @RequestBody MemberDTO member) {
        memberAppService.updateMember(id, member);
        return findMember(id);
    }

    @Operation(summary = "更新会员排行榜")
    @PostMapping("ranking/rebuild")
    public void rebuild() {
        memberRankingService.rebuild();
    }

    @Operation(summary = "分页获取用户邀新提成记录")
    @GetMapping("page-inviter-commission")
    public PageResult<InviterCommissionDTO> pageInviterCommission(InviterCommissionQuery query) {
        return inviterCommissionAppService.pageInviterCommission(query);
    }

}
