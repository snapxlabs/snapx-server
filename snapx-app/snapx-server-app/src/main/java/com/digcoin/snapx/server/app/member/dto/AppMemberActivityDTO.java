package com.digcoin.snapx.server.app.member.dto;

import com.digcoin.snapx.domain.restaurant.bo.LatLngDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/3 23:32
 * @description
 */
@Data
public class AppMemberActivityDTO {

    @NotNull(message = "Activity id cannot be null")
    @Schema(description = "活动Id",required = true)
    private Long activityId;

    @NotNull(message = "latLng cannot be null")
    @Schema(description = "经纬度", required = true)
    private LatLngDTO latLng;

    @Schema(hidden = true)
    private Long memberId;
}
