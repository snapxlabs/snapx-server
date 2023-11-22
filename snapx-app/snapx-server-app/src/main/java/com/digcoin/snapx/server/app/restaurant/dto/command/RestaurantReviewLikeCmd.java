package com.digcoin.snapx.server.app.restaurant.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:28
 * @description
 */
@Data
public class RestaurantReviewLikeCmd {

    @NotNull
    @Min(1)
    @Schema(description = "评论 ID", required = true)
    private Long reviewId;

    @NotNull
    @Schema(description = "是否点赞", required = true)
    private Boolean likes;

}
