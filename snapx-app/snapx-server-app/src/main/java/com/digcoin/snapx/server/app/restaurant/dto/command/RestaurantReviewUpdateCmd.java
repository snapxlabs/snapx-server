package com.digcoin.snapx.server.app.restaurant.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/10 22:03
 * @description
 */
@Data
public class RestaurantReviewUpdateCmd {

    @NotNull(message = "reviewId cannot be null")
    @Min(value = 0, message = "reviewId cannot be less tha 0")
    @Schema(description = "评论 ID", required = true)
    private Long reviewId;

    @NotNull(message = "rating cannot be null")
    @Min(value = 1, message = "rating cannot be less than 1")
    @Max(value = 5, message = "rating cannot be more than 5")
    @Schema(description = "评分：1-5", required = true)
    private Integer rating;

    @NotBlank(message = "content cannot be empty")
    @Schema(description = "评价内容", required = true)
    private String content;

}
