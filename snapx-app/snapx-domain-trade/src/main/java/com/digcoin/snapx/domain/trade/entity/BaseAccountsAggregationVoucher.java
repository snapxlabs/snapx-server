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
@TableName("td_base_accounts_aggregation_voucher")
public class BaseAccountsAggregationVoucher {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 凭证类型
     */
    private String voucherType;

    /**
     * 账户主键
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
     * 资产（借记）debit；负债（贷记）credit；
     */
    private BaseAccountsFinancialType financialType;

    /**
     * 累计数额
     */
    private Long amount;

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
