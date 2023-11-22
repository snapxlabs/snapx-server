package com.digcoin.snapx.domain.trade.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BaseAccountRecentDayRanking {

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 记账金额
     */
    private Long amount;

    /**
     * 排名
     */
    private Long rank;

    private BigDecimal amountBigDecimal;

}
