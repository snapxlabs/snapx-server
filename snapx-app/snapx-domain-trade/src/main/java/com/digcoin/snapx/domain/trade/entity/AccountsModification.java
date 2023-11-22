package com.digcoin.snapx.domain.trade.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsCategory;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("td_accounts_modification")
public class AccountsModification {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 账户id
     */
    private Long accountsId;

    /**
     * 账户类型：WALLET 充值钱包; INCOME 陪玩收入; ESSENCE 精华账户;
     */
    private BaseAccountsType accountsType;

    /**
     * 账户类别：available：可用金额账户;  frozen：冻结金额账户；
     */
    private BaseAccountsCategory category;

    /**
     * 凭证
     */
    private String voucher;

    /**
     * 凭证类型
     */
    private String voucherType;

    /**
     * 记账方向：借记（正）debit；贷记（负）credit；
     */
    private BaseAccountsFinancialType direction;

    /**
     * 金额
     */
    private Long amount;

    /**
     * 账户金额修改原因
     */
    private String reason;

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
