package com.digcoin.snapx.server.base.member.service;

import com.digcoin.snapx.domain.trade.constant.BaseAccountsFinancialSubject;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.mapper.BaseAccountsMapper;
import com.digcoin.snapx.domain.trade.service.BaseAccountsDetailsService;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BaseAccountsTotalDebitUpdateService extends BaseAccountsEventHandleService {

    private final BaseAccountsMapper baseAccountsMapper;

    public BaseAccountsTotalDebitUpdateService(BaseAccountsDetailsService baseAccountsDetailsService,
                                               PointsAccountsService pointsAccountsService,
                                               BaseAccountsMapper baseAccountsMapper) {
        super(baseAccountsDetailsService, pointsAccountsService);
        this.baseAccountsMapper = baseAccountsMapper;
    }

    @Override
    protected Boolean willHandle(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        // 管理员修改评价所获积分时，如果是扣除，需要更新累计挣得积分，扣除修改时扣除部分
        return BaseAccountsFinancialSubject.ACCOUNTS_MODIFICATION.equals(accountsDetails.getSubject());
    }

    @Override
    protected void doHandleTransferOutEvent(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        while (true) {
            BaseAccounts accountsEntity = baseAccountsMapper.selectById(accounts.getId());
            int updated = baseAccountsMapper.updateAccountsTotalDebit(
                    accountsEntity.getId(),
                    accountsEntity.getTotalDebit() - accountsDetails.getAmount(),
                    accountsEntity.getTotalDebit());
            if (updated > 0) {
                break;
            }
        }
    }
}
