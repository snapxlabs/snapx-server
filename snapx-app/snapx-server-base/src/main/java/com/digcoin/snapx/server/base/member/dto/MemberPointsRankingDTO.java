package com.digcoin.snapx.server.base.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberPointsRankingDTO {

    @Schema(description = "会员id")
    private Long memberId;

    @Schema(description = "排行榜")
    private Long rank;

    @Schema(description = "会员昵称")
    private String name;

    @Schema(description = "拍照数量")
    private Integer snap;

    @Schema(description = "连续获取积分天数")
    private Integer steak;

    @Schema(description = "最近天点数")
    private BigDecimal last7Days;

    @Schema(description = "会员所有点数")
    private BigDecimal totalPoints;

    @Schema(description = "邀请人数")
    private Long inviteeCount;

}
