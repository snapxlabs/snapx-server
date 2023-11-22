package com.digcoin.snapx.domain.member.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberEventQuery extends Pageable {

    @Schema(description = "会员事件：ACCESS 会员访问事件；SIGN_UP 会员注册事件；")
    private String event;

    @Schema(description = "查询时段开始时间")
    private LocalDateTime startTime;

    @Schema(description = "查询时段结束时间")
    private LocalDateTime endTime;

}
