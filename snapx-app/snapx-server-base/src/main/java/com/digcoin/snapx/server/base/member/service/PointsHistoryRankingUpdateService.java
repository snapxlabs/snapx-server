package com.digcoin.snapx.server.base.member.service;

import com.digcoin.snapx.domain.member.service.MemberPointsHistoryRankingService;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import com.digcoin.snapx.domain.trade.service.BaseAccountsDetailsService;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PointsHistoryRankingUpdateService extends BaseAccountsEventHandleService {

    private final MemberPointsHistoryRankingService memberPointsHistoryRankingService;

    public PointsHistoryRankingUpdateService(BaseAccountsDetailsService baseAccountsDetailsService,
                                             PointsAccountsService pointsAccountsService,
                                             MemberPointsHistoryRankingService memberPointsHistoryRankingService) {
        super(baseAccountsDetailsService, pointsAccountsService);
        this.memberPointsHistoryRankingService = memberPointsHistoryRankingService;
    }

    @Override
    protected Boolean willHandle(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        return BaseAccountsType.POINTS.equals(accounts.getAccountsType());
    }

    @Override
    protected void doHandleTransferInEvent(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        memberPointsHistoryRankingService.updateRanking(accounts.getMemberId(), accountsDetails.getCreateTime(), accountsDetails.getAmount());
    }

    @Override
    protected void doHandleTransferOutEvent(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        memberPointsHistoryRankingService.updateRanking(accounts.getMemberId(), accountsDetails.getCreateTime(), -1 * accountsDetails.getAmount());
    }

}
