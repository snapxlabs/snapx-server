package com.digcoin.snapx.server.admin.restaurant.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/8 10:44
 * @description
 */
@Data
public class RestaurantDeleteRestaurantCmd {

    @NotNull
    @Min(0)
    @Schema(description = "餐厅 ID")
    private Long id;

}
