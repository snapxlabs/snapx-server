package com.digcoin.snapx.domain.trade.bo;

import com.digcoin.snapx.domain.trade.constant.BaseAccountsFinancialSubject;
import com.digcoin.snapx.domain.trade.constant.BaseAccountsVoucherType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SimpleAccountsDetails {

    /**
     * 主键
     */
    private Long id;

    /**
     * 会员用户id
     */
    private Long memberId;

    /**
     * 账户主键
     */
    private Long walletAccountsId;

    /**
     * 转账账户ID
     */
    private Long transactionAccountsId;

    /**
     * 财务科目代码
     * @see BaseAccountsFinancialSubject
     */
    private String subject;

    /**
     * 记账方向：借记（正）debit；贷记（负）credit；
     */
    private BaseAccountsFinancialType direction;

    /**
     * 记账金额
     */
    private BigDecimal amount;

    /**
     * 凭证
     */
    private String voucher;

    /**
     * 凭证类型
     * @see BaseAccountsVoucherType
     */
    private String voucherType;

    /**
     * 交易备注
     */
    private String remarks;

}
