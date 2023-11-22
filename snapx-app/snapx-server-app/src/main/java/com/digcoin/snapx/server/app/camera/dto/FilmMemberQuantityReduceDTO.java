package com.digcoin.snapx.server.app.camera.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/23 17:30
 * @description
 */
@Data
public class FilmMemberQuantityReduceDTO {

    @NotNull(message = "Change quantity cannot be null")
    @Schema(description = "变动数量")
    @Min(value = 1, message = "Change quantity Must be greater than 0")
    private Long changeQuantity;

    @Schema(hidden = true)
    private Long memberId;
}
