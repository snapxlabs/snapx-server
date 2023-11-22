package com.digcoin.snapx.server.app.trade.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.trade.bo.BaseAccountsDetailsQuery;
import com.digcoin.snapx.server.app.trade.dto.PointsAccountsBalanceDTO;
import com.digcoin.snapx.server.base.trade.dto.PointsAccountsDetailsDTO;
import com.digcoin.snapx.server.app.trade.service.PointsAccountsAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "200 - 会员积分账户")
@Slf4j
@SaCheckLogin
@Validated
@RestController
@RequestMapping("/trade/points-accounts")
@RequiredArgsConstructor
public class PointsAccountsController {

    private final PointsAccountsAppService pointsAccountsAppService;

    @Operation(summary = "获取当前用户积分账户余额")
    @GetMapping("find-accounts-balance")
    public PointsAccountsBalanceDTO findAccountsBalance(@Schema(hidden = true) CurrentUser currentUser) {
        return pointsAccountsAppService.findAccountsBalance(currentUser.getId());
    }

    @Operation(summary = "分页获取当前用户积分账户明细")
    @GetMapping("page-accounts-details")
    public PageResult<PointsAccountsDetailsDTO> pageAccountsDetails(@Schema(hidden = true) CurrentUser currentUser, BaseAccountsDetailsQuery query) {
        return pointsAccountsAppService.pageAccountsDetails(currentUser.getId(), query);
    }

}
