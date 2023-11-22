package com.digcoin.snapx.server.admin.restaurant.dto;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/27 19:08
 * @description
 */
@Data
public class AdminReviewCommentsPageDTO extends Pageable {

    @NotNull(message = "Review Id Can Not Be Null")
    @Schema(description = "评价Id", required = true)
    private Long reviewId;

    @Schema(description = "留言会员昵称")
    private String fromMemberName;

    @Schema(description = "留言会员账号")
    private String fromMemberAccount;

    @Schema(description = "留言内容")
    private String content;
}
