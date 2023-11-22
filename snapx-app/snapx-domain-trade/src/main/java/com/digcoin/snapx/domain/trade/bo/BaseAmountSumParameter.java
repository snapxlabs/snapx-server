package com.digcoin.snapx.domain.trade.bo;

import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import lombok.Builder;
import lombok.Data;

/**
 * @author gengyang.chen
 * @version 1.0.0
 * @ClassName AmountSumParameter.java
 * @Description
 * @createTime 2021/10/7 3:27 下午
 */
@Data
@Builder
public class BaseAmountSumParameter {

    private Long memberId;

    private BaseAccountsType accountsType;

    private Long walletAccountsId;

    private String subject;

    private BaseAccountsFinancialType direction;

}
