package com.digcoin.snapx.domain.trade.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digcoin.snapx.domain.trade.constant.BaseAccountsFinancialSubject;
import com.digcoin.snapx.domain.trade.constant.BaseAccountsVoucherType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsCategory;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员钱包账户明细
 */
@Data
@TableName("td_base_accounts_details")
public class BaseAccountsDetails {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 账户主键
     */
    private Long walletAccountsId;

    /**
     * 账户类型：POINTS 邀新奖励积分账户;
     */
    private BaseAccountsType accountsType;

    /**
     * 账户类别：available：可用金额账户;  frozen：冻结金额账户；
     */
    private BaseAccountsCategory category;

    /**
     * 会员用户id
     */
    private Long memberId;

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
    private Long amount;

    /**
     * 记账前余额
     */
    private Long balanceBefore;

    /**
     * 记账后余额
     */
    private Long balanceAfter;

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

    /**
     * 客户端可见标识 1 可见 0 不可见
     */
    private Boolean customerVisible;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建操作人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新操作人id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 删除时间，值为0则未删除
     */
    private Long deleted;

}
