package com.digcoin.snapx.domain.trade.service;

import com.digcoin.snapx.domain.trade.constant.BaseAccountsFinancialSubject;
import com.digcoin.snapx.domain.trade.constant.BaseAccountsVoucherType;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsCategory;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import com.digcoin.snapx.domain.trade.mapper.AccountsModificationMapper;
import com.digcoin.snapx.domain.trade.mapper.BaseAccountsAggregationVoucherMapper;
import com.digcoin.snapx.domain.trade.mapper.BaseAccountsDetailsMapper;
import com.digcoin.snapx.domain.trade.mapper.BaseAccountsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class PointsAccountsService extends BaseAccountsService {

    private static final BigDecimal RATIO = new BigDecimal(100);
    private static final Integer SCALE = 2;

    public PointsAccountsService(BaseAccountsMapper baseAccountsMapper,
                                 BaseAccountsDetailsMapper baseAccountsDetailsMapper,
                                 BaseAccountsDetailsService baseAccountsDetailsService,
                                 BaseAccountsAggregationVoucherMapper baseAccountsAggregationVoucherMapper,
                                 AccountsModificationMapper accountsModificationMapper) {
        super(baseAccountsMapper, baseAccountsDetailsMapper, baseAccountsDetailsService, baseAccountsAggregationVoucherMapper, accountsModificationMapper);
    }

    @Override
    protected BaseAccountsType getAccountsType() {
        return BaseAccountsType.POINTS;
    }

    /**
     * 管理员修改餐馆评价奖励点数
     * 修改后点数比之前大时调用该方法增加点数
     *
     * @param memberId   餐馆评价所属用户id
     * @param evaluateId 餐馆评价id
     * @param amount     相较于原奖励点数增加的部分
     */
    public void increasePointsByAdminModify(Long memberId, Long evaluateId, BigDecimal amount) {
        BaseAccounts walletAccounts = findAccounts(memberId, BaseAccountsCategory.AVAILABLE);
        long id = createAccountsModification(walletAccounts, BaseAccountsFinancialType.DEBIT,
                BaseAccountsVoucherType.RESTAURANT_EVALUATE, String.valueOf(evaluateId), amount, "increase by admin while modify RESTAURANT EVALUATE");
        increasePoints(walletAccounts,
                BaseAccountsFinancialSubject.ACCOUNTS_MODIFICATION,
                BaseAccountsVoucherType.ACCOUNTS_MODIFICATION,
                String.valueOf(id),
                amount);
    }

    /**
     * 管理员修改餐馆评价奖励点数
     * 修改后点数比之前小时调用该方法减少点数
     *
     * @param memberId   餐馆评价所属用户id
     * @param evaluateId 餐馆评价id
     * @param amount     相较于原奖励点数减少的部分
     */
    public void reducePointsByAdminModify(Long memberId, Long evaluateId, BigDecimal amount) {
        BaseAccounts walletAccounts = findAccounts(memberId, BaseAccountsCategory.AVAILABLE);
        long id = createAccountsModification(walletAccounts, BaseAccountsFinancialType.CREDIT,
                BaseAccountsVoucherType.RESTAURANT_EVALUATE, String.valueOf(evaluateId), amount, "reduce by admin while modify RESTAURANT EVALUATE");
        reducePoints(walletAccounts,
                BaseAccountsFinancialSubject.ACCOUNTS_MODIFICATION,
                BaseAccountsVoucherType.ACCOUNTS_MODIFICATION,
                String.valueOf(id),
                amount);
    }

    @Override
    protected BigDecimal doTranslate(Long amount) {
        return BigDecimal.valueOf(amount).divide(RATIO, SCALE, RoundingMode.HALF_UP);
    }

    @Override
    protected Long doTranslate(BigDecimal amount) {
        return amount.multiply(RATIO).longValue();
    }
}
