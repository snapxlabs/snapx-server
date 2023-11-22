package com.digcoin.snapx.domain.trade.bo;

import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class BaseAccountsDetailsFilter {

    private Long baseAccountsId;

    private BaseAccountsType accountsType;

    private BaseAccountsFinancialType direction;

    private String voucherType;

    private String voucher;

    private Collection<String> vouchers;

    private Collection<Long> ids;

}
