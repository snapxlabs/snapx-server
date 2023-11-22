package com.digcoin.snapx.domain.trade.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsCategory;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员钱包账户
 */
@Data
@TableName("td_base_accounts")
public class BaseAccounts {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 账户类型：POINTS 邀新奖励积分账户;
     */
    private BaseAccountsType accountsType;

    /**
     * 账户类别：available：可用金额账户;  frozen：冻结金额账户；
     */
    private BaseAccountsCategory category;

    /**
     * 资产（借记）debit；负债（贷记）credit；
     */
    private BaseAccountsFinancialType financialType;

    /**
     * 账户余额（单位分）
     */
    private Long balance;

    /**
     * 累计资产（借记）（单位分）
     */
    private Long totalDebit;

    /**
     * 累计负债（贷记）（单位分）
     */
    private Long totalCredit;

    /**
     * 会员用户id
     */
    private Long memberId;

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
