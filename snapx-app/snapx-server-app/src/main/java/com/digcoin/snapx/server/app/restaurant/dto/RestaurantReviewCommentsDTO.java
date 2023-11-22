package com.digcoin.snapx.server.app.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/24 18:59
 * @description
 */
@Data
public class RestaurantReviewCommentsDTO {

    @Schema(description = "评论Id", required = true)
    @NotNull(message = "Review id can not be null")
    private Long reviewId;

    @Schema(description = "父级留言Id")
    private Long parentCommentsId;

    @Schema(description = "留言内容", required = true)
    @NotBlank(message = "Comments content can not be null")
    private String content;

    @Schema(hidden = true)
    private Long fromMemberId;

    @Schema(hidden = true)
    private String fromMemberName;
}
