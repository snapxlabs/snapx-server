package com.digcoin.snapx.domain.restaurant.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/27 19:12
 * @description
 */
@Data
public class RestaurantReviewCommentsQueryBO extends Pageable {

    @Schema(description = "评价Id")
    private Long reviewId;

    @Schema(description = "留言会员昵称")
    private String fromMemberName;

    @Schema(description = "留言会员账号")
    private String fromMemberAccount;

    @Schema(description = "留言内容")
    private String content;
}
