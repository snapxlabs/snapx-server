package com.digcoin.snapx.server.admin.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberEventStatisticsDTO {

    @Schema(description = "今日独立访问用户数")
    private Long dailyAccess;

    @Schema(description = "最近24小时独立访问用户数")
    private Long recentDailyAccess;

    @Schema(description = "今日访问用户数")
    private Long dailyVisit;

    @Schema(description = "最近24小时访问用户数")
    private Long recentDailyVisit;

    @Schema(description = "今月独立访问用户数")
    private Long monthAccess;

    @Schema(description = "最近30天独立访问用户数")
    private Long recentMonthAccess;

    @Schema(description = "今月访问用户数")
    private Long monthVisit;

    @Schema(description = "最近30天访问用户数")
    private Long recentMonthVisit;

    @Schema(description = "总访问量")
    private Long totalVisit;

    @Schema(description = "今日注册用户数")
    private Long dailySignUp;

    @Schema(description = "最近24小时注册用户数")
    private Long recentDailySignUp;

    @Schema(description = "今月注册用户数")
    private Long monthSignUp;

    @Schema(description = "最近30天注册用户数")
    private Long recentMonthSignUp;

}
