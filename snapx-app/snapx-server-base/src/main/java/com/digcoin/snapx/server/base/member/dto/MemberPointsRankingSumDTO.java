package com.digcoin.snapx.server.base.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberPointsRankingSumDTO {

    @Schema(description = "总积分数")
    private BigDecimal totalPointsCount;

    @Schema(description = "总拍照数")
    private Long totalPhotoCont;

    @Schema(description = "总连续拍照天数")
    private Long totalSteakCount;

}
