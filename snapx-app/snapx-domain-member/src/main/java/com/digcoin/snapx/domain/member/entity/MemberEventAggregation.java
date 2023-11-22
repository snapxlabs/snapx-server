package com.digcoin.snapx.domain.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员行为事件统计（每小时）
 */
@Data
@TableName("mem_member_event_aggregation")
public class MemberEventAggregation {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 会员事件：ACCESS 会员访问事件；SIGN_UP 会员注册事件；
     */
    private String event;

    /**
     * 事件发生时间
     */
    private LocalDateTime time;

    /**
     * 统计汇集数量
     */
    private Long count;


}
