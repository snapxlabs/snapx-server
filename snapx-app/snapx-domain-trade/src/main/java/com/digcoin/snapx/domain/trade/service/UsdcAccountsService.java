package com.digcoin.snapx.domain.trade.service;

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
public class UsdcAccountsService extends BaseAccountsService {

    private static final BigDecimal RATIO = new BigDecimal(100);
    private static final Integer SCALE = 2;

    public UsdcAccountsService(BaseAccountsMapper baseAccountsMapper,
                               BaseAccountsDetailsMapper baseAccountsDetailsMapper,
                               BaseAccountsDetailsService baseAccountsDetailsService,
                               BaseAccountsAggregationVoucherMapper baseAccountsAggregationVoucherMapper,
                               AccountsModificationMapper accountsModificationMapper) {
        super(baseAccountsMapper, baseAccountsDetailsMapper, baseAccountsDetailsService, baseAccountsAggregationVoucherMapper, accountsModificationMapper);
    }

    @Override
    protected BaseAccountsType getAccountsType() {
        return BaseAccountsType.USDC;
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
