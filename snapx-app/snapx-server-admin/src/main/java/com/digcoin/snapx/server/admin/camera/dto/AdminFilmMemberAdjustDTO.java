package com.digcoin.snapx.server.admin.camera.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/1 10:31
 * @description
 */
@Data
public class AdminFilmMemberAdjustDTO {

    @Schema(description = "会员Id")
    @NotNull(message = "Member id cannot be null")
    private Long memberId;

    @Schema(description = "调整数量")
    @NotNull(message = "Adjust quantity cannot be null")
    private Long adjustQuantity;

    @Schema(hidden = true)
    private Long adminUserId;
}
