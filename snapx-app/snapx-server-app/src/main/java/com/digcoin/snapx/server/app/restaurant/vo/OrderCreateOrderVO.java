package com.digcoin.snapx.server.app.restaurant.vo;

import com.digcoin.snapx.server.base.member.dto.BaseGiftCountDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:22
 * @description
 */
@AllArgsConstructor
@Data
public class OrderCreateOrderVO {

    @Schema(description = "订单 ID")
    private Long orderId;

    @Schema(description = "评价 ID")
    private Long reviewId;

    @Schema(description = "赚取")
    private BigDecimal earned;

    @Schema(description = "收入单位")
    private String earnedUnit;

    @Schema(description = "货币代码")
    private String currencyCode;

    @Schema(description = "赠送详情")
    private BaseGiftCountDTO baseGiftCount;

}
