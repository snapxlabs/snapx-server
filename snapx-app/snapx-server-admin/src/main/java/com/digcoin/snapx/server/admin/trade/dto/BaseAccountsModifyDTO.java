package com.digcoin.snapx.server.admin.trade.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BaseAccountsModifyDTO {

    @Schema(description = "会员id")
    private Long memberId;

    @Schema(description = "数额")
    private BigDecimal amount;

    @Schema(description = "修改原因")
    private String reason;

}
