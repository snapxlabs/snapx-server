package com.digcoin.snapx.server.app.restaurant.dto.command.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:38
 * @description
 */
@ParameterObject
@Data
public class RestaurantFindRestaurantQry {

    @NotNull
    @Min(1)
    @Schema(description = "餐厅 ID", required = true)
    private Long restaurantId;

}
