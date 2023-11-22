package com.digcoin.snapx.server.app.restaurant.dto;

import com.digcoin.snapx.server.base.member.dto.BaseGiftCountDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/10 23:12
 * @description
 */
@Data
public class RestaurantReviewDTO {

    @Schema(description = "评价 ID")
    private Long reviewId;

    @Schema(description = "餐厅 ID")
    private Long restaurantId;

    @Schema(description = "餐厅名称")
    private String restaurantName;

    @Schema(description = "会员 ID")
    private Long memberId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "评分：1-5")
    private Integer rating;

    @Schema(description = "花费")
    private BigDecimal spent;

    @Schema(description = "货币代码")
    private String currencyCode;

    @Schema(description = "赚取")
    private BigDecimal earned;

    @Schema(description = "收入单位")
    private String earnedUnit;

    @Schema(description = "照片地址列表")
    private List<String> photoUrls;

    @Schema(description = "评价内容")
    private String content;

    @Schema(description = "评价时间", type = "string")
    private LocalDateTime createTime;

    @Schema(description = "点赞数")
    private Long likesNum;

    @Schema(description = "是否点赞")
    private Boolean likes;

    @Schema(description = "餐厅")
    private RestaurantDTO restaurant;

    @Schema(description = "赠送详情")
    private BaseGiftCountDTO baseGiftCount;

    @Schema(description = "是否允许编辑")
    private Boolean allowModification;

    @Schema(description = "留言数量")
    private Long commentsNum;

}
