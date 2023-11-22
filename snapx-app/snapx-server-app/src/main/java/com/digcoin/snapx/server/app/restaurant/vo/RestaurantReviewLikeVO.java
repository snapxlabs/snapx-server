package com.digcoin.snapx.server.app.restaurant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:18
 * @description
 */
@AllArgsConstructor
@Data
public class RestaurantReviewLikeVO {

    @Schema(description = "是否点赞")
    private Boolean status;

    @Schema(description = "点赞数")
    private Long likesNum;

}
