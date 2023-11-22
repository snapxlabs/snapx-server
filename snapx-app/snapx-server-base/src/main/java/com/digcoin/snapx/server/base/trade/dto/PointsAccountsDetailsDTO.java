package com.digcoin.snapx.server.base.trade.dto;

import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PointsAccountsDetailsDTO {

    /**
     * 财务科目代码
     * @see com.digcoin.snapx.domain.trade.constant.BaseAccountsFinancialSubject
     */
    @Schema(description = "入账来源类型：RESTAURANT_EVALUATE 餐馆评价奖励; INVITE_USERS 邀请用户记录奖励;")
    private String subject;

    @Schema(description = "记账方向：借记（正）debit；贷记（负）credit；")
    private BaseAccountsFinancialType direction;

    @Schema(description = "记账点数")
    private BigDecimal amount;

    @Schema(description = "记账前点数")
    private BigDecimal balanceBefore;

    @Schema(description = "记账点数")
    private BigDecimal balanceAfter;

    /**
     * 凭证类型
     * @see com.digcoin.snapx.domain.trade.constant.BaseAccountsVoucherType
     */
    @Schema(description = "凭证类型：RESTAURANT_EVALUATE 餐馆评价id; INVITE_USERS 邀请用户记录id;")
    private String voucherType;

    @Schema(description = "凭证")
    private String voucher;

    @Schema(description = "备注信息")
    private String remarks;

    @Schema(description = "创建时间",
            type = "string",
            format = "date-time",
            accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;
}
