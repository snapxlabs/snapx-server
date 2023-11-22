package com.digcoin.snapx.server.base.member.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.digcoin.snapx.domain.trade.constant.BaseAccountsFinancialSubject;
import com.digcoin.snapx.domain.trade.constant.BaseAccountsVoucherType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsCategory;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsFinancialType;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InviterCommissionDTO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "邀请人")
    private MemberDTO inviter;

    @Schema(description = "被邀请人")
    private MemberDTO invitee;

    @Schema(description = "被邀请人收入金额")
    private BigDecimal inviteeIncomeAmount;

    @Schema(description = "邀请人分佣金额")
    private BigDecimal inviterCommissionAmount;

    @Schema(description = "分佣比例")
    private String sharingRatio;

    @Schema(description = "交易明细")
    private BaseAccountsDetailsDTO details;

    @Data
    public static class MemberDTO {

        @Schema(description = "主键")
        private Long id;

        @Schema(description = "账号")
        private String account;

        @Schema(description = "昵称")
        private String nickname;

        @Schema(description = "头像URL地址")
        private String avatar;

        @Schema(description = "用户主页背景图地址")
        private String avatarCover;

    }

    @Data
    public static class BaseAccountsDetailsDTO {

        @Schema(description = "主键")
        private Long id;

        @Schema(description = "财务科目代码")
        private String subject;

        @Schema(description = "记账方向：借记（正）debit；贷记（负）credit；")
        private BaseAccountsFinancialType direction;

        @Schema(description = "凭证")
        private String voucher;

        @Schema(description = "凭证类型")
        private String voucherType;

        @Schema(description = "交易备注")
        private String remarks;

    }

}
