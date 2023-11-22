package com.digcoin.snapx.server.app.restaurant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/24 18:58
 * @description
 */
@Data
public class RestaurantReviewCommentsVO {

    @Schema(description = "留言Id")
    private Long Id;

    @Schema(description = "评论Id")
    private Long reviewId;

    @Schema(description = "父级留言Id")
    private Long parentCommentsId;

    @Schema(description = "父级留言的会员Id")
    private Long parentMemberId;

    @Schema(description = "父级留言的会员名称")
    private String parentMemberName;

    @Schema(description = "留言者Id")
    private Long fromMemberId;

    @Schema(description = "留言者名称")
    private String fromMemberName;

    @Schema(description = "留言内容")
    private String content;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", type = "string")
    private LocalDateTime updateTime;
}
