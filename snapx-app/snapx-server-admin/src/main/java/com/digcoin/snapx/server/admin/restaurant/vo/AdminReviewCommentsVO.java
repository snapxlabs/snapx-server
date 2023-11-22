package com.digcoin.snapx.server.admin.restaurant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/27 19:09
 * @description
 */
@Data
public class AdminReviewCommentsVO {

    @Schema(description = "留言Id")
    private Long id;

    @Schema(description = "评论Id")
    private Long reviewId;

    @Schema(description = "父级留言Id")
    private Long parentCommentsId;

    @Schema(description = "父级留言的会员Id")
    private Long parentMemberId;

    @Schema(description = "留言者Id")
    private Long fromMemberId;

    @Schema(description = "留言者昵称")
    private String fromMemberNickName;

    @Schema(description = "留言者账号")
    private String fromMemberAccount;

    @Schema(description = "留言内容")
    private String content;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", type = "string")
    private LocalDateTime updateTime;

}
