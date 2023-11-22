package com.digcoin.snapx.server.app.trade.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PointsAccountsBalanceDTO {

    @Schema(description = "积分合计")
    private BigDecimal total;

}
