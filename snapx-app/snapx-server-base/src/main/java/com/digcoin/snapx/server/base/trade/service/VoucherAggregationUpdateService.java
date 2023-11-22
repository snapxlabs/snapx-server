package com.digcoin.snapx.server.base.trade.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsAggregationVoucher;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsCategory;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import com.digcoin.snapx.domain.trade.mapper.BaseAccountsAggregationVoucherMapper;
import com.digcoin.snapx.domain.trade.service.BaseAccountsDetailsService;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import com.digcoin.snapx.server.base.member.service.BaseAccountsEventHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class VoucherAggregationUpdateService extends BaseAccountsEventHandleService {

    private final BaseAccountsAggregationVoucherMapper baseAccountsAggregationVoucherMapper;

    public VoucherAggregationUpdateService(BaseAccountsDetailsService baseAccountsDetailsService,
                                           PointsAccountsService pointsAccountsService,
                                           BaseAccountsAggregationVoucherMapper baseAccountsAggregationVoucherMapper) {
        super(baseAccountsDetailsService, pointsAccountsService);
        this.baseAccountsAggregationVoucherMapper = baseAccountsAggregationVoucherMapper;
    }

    @Override
    protected Boolean willHandle(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        return true;
    }

    @Override
    protected void doHandleTransferInEvent(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        updateVoucherAggregation(accounts, accountsDetails);
    }

    @Override
    protected void doHandleTransferOutEvent(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        updateVoucherAggregation(accounts, accountsDetails);
    }

    private void updateVoucherAggregation(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        Long accountsId = accounts.getId();
        BaseAccountsType type = accounts.getAccountsType();
        BaseAccountsCategory category = accounts.getCategory();
        String voucherType = accountsDetails.getVoucherType();
        BaseAccountsFinancialType direction = accountsDetails.getDirection();
        Long amount = accountsDetails.getAmount();
        Long memberId = accounts.getMemberId();

        BaseAccountsAggregationVoucher baseAccountsAggregationVoucher = baseAccountsAggregationVoucherMapper.selectOne(
                Wrappers.lambdaQuery(BaseAccountsAggregationVoucher.class)
                        .eq(BaseAccountsAggregationVoucher::getAccountsId, accountsId)
                        .eq(BaseAccountsAggregationVoucher::getVoucherType, voucherType)
                        .eq(BaseAccountsAggregationVoucher::getFinancialType, direction));
        if (Objects.isNull(baseAccountsAggregationVoucher)) {
            BaseAccountsAggregationVoucher entity = new BaseAccountsAggregationVoucher();
            entity.setAccountsId(accountsId);
            entity.setAccountsType(type);
            entity.setCategory(category);
            entity.setVoucherType(voucherType);
            entity.setFinancialType(direction);
            entity.setAmount(amount);
            entity.setMemberId(memberId);
            baseAccountsAggregationVoucherMapper.insert(entity);
            return;
        }

        while (true) {
            int updated = baseAccountsAggregationVoucherMapper.update(new BaseAccountsAggregationVoucher(),
                    Wrappers.lambdaUpdate(BaseAccountsAggregationVoucher.class)
                            .set(BaseAccountsAggregationVoucher::getAmount, baseAccountsAggregationVoucher.getAmount() + amount)
                            .eq(BaseAccountsAggregationVoucher::getId, baseAccountsAggregationVoucher.getId())
                            .eq(BaseAccountsAggregationVoucher::getAmount, baseAccountsAggregationVoucher.getAmount()));
            if (updated > 0) {
                break;
            }
            baseAccountsAggregationVoucher = baseAccountsAggregationVoucherMapper.selectById(baseAccountsAggregationVoucher.getId());
        }
    }
}
