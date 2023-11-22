package com.digcoin.snapx.server.base.member.dto;

import com.digcoin.snapx.core.common.constant.GiftUnit;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/10 17:52
 * @description
 */
@Data
public class BaseGiftCountUnitDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "积分单位" , accessMode = Schema.AccessMode.READ_ONLY)
    private String pointsUnit = GiftUnit.POINT_UNIT;;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "usdc单位" , accessMode = Schema.AccessMode.READ_ONLY)
    private String usdcUnit = GiftUnit.USDC_UNIT;

}
