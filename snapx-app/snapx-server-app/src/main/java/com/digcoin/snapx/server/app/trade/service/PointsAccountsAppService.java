package com.digcoin.snapx.server.app.trade.service;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.trade.bo.BaseAccountsDetailsQuery;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsCategory;
import com.digcoin.snapx.domain.trade.service.BaseAccountsDetailsService;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import com.digcoin.snapx.server.base.trade.converter.PointsAccountsDetailsConverter;
import com.digcoin.snapx.server.app.trade.dto.PointsAccountsBalanceDTO;
import com.digcoin.snapx.server.base.trade.dto.PointsAccountsDetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointsAccountsAppService {

    private final PointsAccountsService pointsAccountsService;
    private final BaseAccountsDetailsService walletAccountsDetailsService;
    private final PointsAccountsDetailsConverter pointsAccountsDetailsConverter;

    public PointsAccountsBalanceDTO findAccountsBalance(Long memberId) {
        BigDecimal accountsBalance = pointsAccountsService.getBalance(memberId);
        PointsAccountsBalanceDTO result = new PointsAccountsBalanceDTO();
        result.setTotal(accountsBalance);
        return result;
    }

    public PageResult<PointsAccountsDetailsDTO> pageAccountsDetails(Long memberId, BaseAccountsDetailsQuery query) {
        BaseAccounts availableAccounts = pointsAccountsService.findAccounts(memberId, BaseAccountsCategory.AVAILABLE);
        query.setWalletAccountsId(availableAccounts.getId());
        query.setCustomerVisible(true);
        PageResult<BaseAccountsDetails> pageResult = walletAccountsDetailsService.pageBaseAccountsDetails(query);
        return PageResult.fromPageResult(pageResult, pointsAccountsDetailsConverter::intoDTO);
    }

}
