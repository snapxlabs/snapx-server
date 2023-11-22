package com.digcoin.snapx.server.admin.member.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/6 14:34
 * @description
 */
@Data
public class AdminMemberActivityVO {

    @Schema(description = "会员活动Id")
    private Long id;

    @Schema(description = "会员Id")
    private Long memberId;

    @Schema(description = "会员账号")
    private String memberAccount;

    @Schema(description = "活动Id")
    private Long activityId;

    @Schema(description = "活动名称")
    private String activityName;

    @Schema(description = "参与活动时纬度")
    private String lat;

    @Schema(description = "参与活动时经度")
    private String lng;

    @Schema(description = "赠送的积分数量")
    private BigDecimal giftPointsCount;

    @Schema(description = "赠送的usdc数量")
    private BigDecimal giftUsdcCount;

    @Schema(description = "精选赠送的积分数量")
    private BigDecimal specGiftPointsCount;

    @Schema(description = "精选赠送的usdc数量")
    private BigDecimal specGiftUsdcCount;

    @Schema(description = "精选额外赠送的usdc数量")
    private BigDecimal specGiftExtraUsdcCount;

    @Schema(description = "赠送积分总数量")
    private BigDecimal totalGiftPointsCount;

    @Schema(description = "赠送usdc总数量")
    private BigDecimal totalGiftUsdcCount;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", type = "string")
    private LocalDateTime updateTime;

}
