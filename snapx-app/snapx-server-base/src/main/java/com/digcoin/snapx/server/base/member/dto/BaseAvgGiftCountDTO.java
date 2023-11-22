package com.digcoin.snapx.server.base.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/10 17:51
 * @description
 */
@Data
public class BaseAvgGiftCountDTO extends BaseGiftCountUnitDTO{

    @Schema(description = "所有参与活动/餐厅评价赠送平均总数量")
    private BigDecimal avgGiftPointsCount;

    @Schema(description = "所有参与活动/餐厅评价赠送平均数量")
    private BigDecimal avgGiftUsdcCount;
}
