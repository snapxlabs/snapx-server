package com.digcoin.snapx.server.app.member.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.service.BaseAccountsService;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import com.digcoin.snapx.server.base.member.dto.MemberPointsRankingDTO;
import com.digcoin.snapx.server.base.member.dto.MemberPointsRankingSumDTO;
import com.digcoin.snapx.server.base.member.service.MemberRankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Tag(name = "104 - 会员 - 积分排行榜")
@Slf4j
@SaCheckLogin
@Validated
@RestController
@RequestMapping("/member/ranking")
@RequiredArgsConstructor
public class MemberRankingController {

    private final MemberRankingService memberRankingService;

    @SaIgnore
    @Operation(summary = "获取用户积分排汇总")
    @GetMapping("get-member-points-ranking-sum")
    public MemberPointsRankingSumDTO getMemberPointsRankingSum() {
        return memberRankingService.getMemberPointsRankingSum();
    }

    @SaIgnore
    @Operation(summary = "分页获取用户积分排行榜")
    @GetMapping("page-member-points-ranking")
    public PageResult<MemberPointsRankingDTO> pageMemberPointsRanking(@Schema(description = "页码") @RequestParam Integer page,
                                                                      @Schema(description = "每页记录数") @RequestParam Integer pageSize) {
        return memberRankingService.pageMemberPointsRankingV2(page, pageSize);
    }

}
