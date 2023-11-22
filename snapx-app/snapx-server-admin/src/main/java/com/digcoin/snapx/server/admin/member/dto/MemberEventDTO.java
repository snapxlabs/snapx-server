package com.digcoin.snapx.server.admin.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberEventDTO {

    @Schema(description = "会员事件：ACCESS 会员访问事件；SIGN_UP 会员注册事件；")
    private String event;

    @Schema(description = "事件发生时间")
    private LocalDateTime time;

    @Schema(description = "统计汇集数量")
    private Long count;

}
