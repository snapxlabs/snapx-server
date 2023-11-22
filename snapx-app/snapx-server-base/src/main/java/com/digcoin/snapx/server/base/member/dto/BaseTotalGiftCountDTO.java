package com.digcoin.snapx.server.base.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/8 23:26
 * @description
 */
@Data
public class BaseTotalGiftCountDTO extends BaseGiftCountUnitDTO{

    @Schema(description = "所有参与活动/餐厅评价赠送积分总数量")
    private BigDecimal totalGiftPointsCount;

    @Schema(description = "所有参与活动/餐厅评价赠送usdc总数量")
    private BigDecimal totalGiftUsdcCount;

}
