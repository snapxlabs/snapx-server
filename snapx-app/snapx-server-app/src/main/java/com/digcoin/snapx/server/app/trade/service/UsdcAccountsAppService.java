package com.digcoin.snapx.server.app.trade.service;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.trade.bo.BaseAccountsDetailsQuery;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsCategory;
import com.digcoin.snapx.domain.trade.service.BaseAccountsDetailsService;
import com.digcoin.snapx.domain.trade.service.UsdcAccountsService;
import com.digcoin.snapx.server.base.trade.converter.UsdcAccountsDetailsConverter;
import com.digcoin.snapx.server.app.trade.dto.PointsAccountsBalanceDTO;
import com.digcoin.snapx.server.base.trade.dto.UsdcAccountsDetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsdcAccountsAppService {

    private final UsdcAccountsService usdcAccountsService;
    private final BaseAccountsDetailsService walletAccountsDetailsService;
    private final UsdcAccountsDetailsConverter usdcAccountsDetailsConverter;

    public PointsAccountsBalanceDTO findAccountsBalance(Long memberId) {
        BigDecimal accountsBalance = usdcAccountsService.getBalance(memberId);
        PointsAccountsBalanceDTO result = new PointsAccountsBalanceDTO();
        result.setTotal(accountsBalance);
        return result;
    }

    public PageResult<UsdcAccountsDetailsDTO> pageAccountsDetails(Long memberId, BaseAccountsDetailsQuery query) {
        BaseAccounts availableAccounts = usdcAccountsService.findAccounts(memberId, BaseAccountsCategory.AVAILABLE);
        query.setWalletAccountsId(availableAccounts.getId());
        query.setCustomerVisible(true);
        PageResult<BaseAccountsDetails> pageResult = walletAccountsDetailsService.pageBaseAccountsDetails(query);
        return PageResult.fromPageResult(pageResult, usdcAccountsDetailsConverter::intoDTO);
    }

}
