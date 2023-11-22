package com.digcoin.snapx.server.base.member.service;

import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.event.BaseAccountsTransferInEvent;
import com.digcoin.snapx.domain.trade.event.BaseAccountsTransferOutEvent;
import com.digcoin.snapx.domain.trade.service.BaseAccountsDetailsService;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;

@Slf4j
public abstract class BaseAccountsEventHandleService {

    protected final BaseAccountsDetailsService baseAccountsDetailsService;
    protected final PointsAccountsService pointsAccountsService;

    public BaseAccountsEventHandleService(BaseAccountsDetailsService baseAccountsDetailsService, PointsAccountsService pointsAccountsService) {
        this.baseAccountsDetailsService = baseAccountsDetailsService;
        this.pointsAccountsService = pointsAccountsService;
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void handleTransferInEvent(BaseAccountsTransferInEvent event) {
        BaseAccountsDetails accountsDetails = baseAccountsDetailsService.findAccountsDetails(event.getBaseAccountsDetailsId());
        if (Objects.isNull(accountsDetails)) {
            return;
        }
        Long baseAccountsId = accountsDetails.getWalletAccountsId();
        BaseAccounts accounts = pointsAccountsService.findAccounts(baseAccountsId);
        if (Objects.isNull(accounts)) {
            return;
        }
        if (!willHandle(accounts, accountsDetails)) {
            return;
        }

        doHandleTransferInEvent(accounts, accountsDetails);
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void handleTransferOutEvent(BaseAccountsTransferOutEvent event) {
        BaseAccountsDetails accountsDetails = baseAccountsDetailsService.findAccountsDetails(event.getBaseAccountsDetailsId());
        if (Objects.isNull(accountsDetails)) {
            return;
        }
        Long baseAccountsId = accountsDetails.getWalletAccountsId();
        BaseAccounts accounts = pointsAccountsService.findAccounts(baseAccountsId);
        if (Objects.isNull(accounts)) {
            return;
        }
        if (!willHandle(accounts, accountsDetails)) {
            return;
        }

        doHandleTransferOutEvent(accounts, accountsDetails);
    }

    protected abstract Boolean willHandle(BaseAccounts accounts, BaseAccountsDetails accountsDetails);
    protected void doHandleTransferInEvent(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {

    }
    protected void doHandleTransferOutEvent(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {

    }

}
