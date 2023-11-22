package com.digcoin.snapx.server.admin.restaurant.dto.command.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/28 10:05
 * @description
 */
@Data
public class RestaurantFindReviewQry {

    @NotNull
    @Min(0)
    @Schema(description = "评价 ID")
    private Long id;

}
