package com.digcoin.snapx.domain.trade.bo;

import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author gengyang.chen
 * @version 1.0.0
 * @ClassName AmountSumParameter.java
 * @Description
 * @createTime 2021/10/7 3:27 下午
 */
@Data
@Builder
public class BaseAmountSumByDatetimeParameter {

    private Collection<Long> memberIds;

    private BaseAccountsType accountsType;

    private Long walletAccountsId;

    private String subject;

    private BaseAccountsFinancialType direction;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
