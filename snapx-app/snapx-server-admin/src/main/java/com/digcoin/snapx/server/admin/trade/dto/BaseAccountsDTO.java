package com.digcoin.snapx.server.admin.trade.dto;

import com.digcoin.snapx.server.admin.member.dto.MemberDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BaseAccountsDTO {

    @Schema(description = "会员信息")
    private MemberDTO member;

    @Schema(description = "账户详情")
    private Accounts accounts;

    @Data
    public static class Accounts {
        @Schema(description = "余额")
        private BigDecimal balance;
    }

}
