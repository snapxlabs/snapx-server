package com.digcoin.snapx.domain.trade.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BaseAccountsBalance {

    @Schema(description = "钱包余额合计")
    private BigDecimal total = BigDecimal.ZERO;

    @Schema(description = "可用余额")
    private BigDecimal available = BigDecimal.ZERO;

    @Schema(description = "冻结余额")
    private BigDecimal frozen = BigDecimal.ZERO;

}
