package com.digcoin.snapx.server.base.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/7 15:26
 * @description
 */
@Data
public class BaseGiftCountDTO extends BaseGiftCountUnitDTO{

    @Schema(description = "单次参与活动/评价赠送的积分数量")
    private BigDecimal giftPointsCount;

    @Schema(description = "单次参与活动/评价赠送的usdc数量")
    private BigDecimal giftUsdcCount;

    @Schema(description = "单次参与精选活动/精选评价赠送的积分数量")
    private BigDecimal specGiftPointsCount;

    @Schema(description = "单次参与精选活动/精选评价赠送的usdc数量")
    private BigDecimal specGiftUsdcCount;

    @Schema(description = "单次参与精选活动/精选评价额外赠送的usdc数量")
    private BigDecimal specGiftExtraUsdcCount;

    @Schema(description = "单次参与活动/评价赠送积分之和")
    private BigDecimal totalGiftPointsCount;

    @Schema(description = "单次参与活动/评价赠送usdc之和")
    private BigDecimal totalGiftUsdcCount;

    @Schema(description = "活动/评价Id")
    private Long businessId;


}
