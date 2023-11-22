package com.digcoin.snapx.server.admin.trade.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.member.bo.MemberQuery;
import com.digcoin.snapx.domain.trade.bo.BaseAccountsDetailsQuery;
import com.digcoin.snapx.server.admin.trade.dto.BaseAccountsDTO;
import com.digcoin.snapx.server.admin.trade.dto.BaseAccountsModifyDTO;
import com.digcoin.snapx.server.admin.trade.service.PointsAccountsAppService;
import com.digcoin.snapx.server.base.trade.dto.PointsAccountsDetailsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "200 - 会员积分账户")
@Slf4j
@SaCheckLogin
@Validated
@RestController
@RequestMapping("/trade/points-accounts")
@RequiredArgsConstructor
public class PointsAccountsController {

    private final PointsAccountsAppService pointsAccountsAppService;

    @GetMapping("page-accounts")
    @Operation(description = "分页查询账户列表")
    public PageResult<BaseAccountsDTO> pageAccounts(MemberQuery query) {
        return pointsAccountsAppService.pageAccounts(query);
    }

    @GetMapping("page-accounts-details")
    @Operation(description = "分页查询用户账户明细")
    public PageResult<PointsAccountsDetailsDTO> pageAccountsDetails(@Schema(description = "会员id") @RequestParam Long memberId,
                                                                    BaseAccountsDetailsQuery query) {
        return pointsAccountsAppService.pageAccountsDetails(memberId, query);
    }

    @PostMapping("update-accounts")
    @Operation(description = "更新账户余额")
    public void updateAccounts(@RequestBody BaseAccountsModifyDTO payload) {
        pointsAccountsAppService.updateAccounts(payload);
    }
}
