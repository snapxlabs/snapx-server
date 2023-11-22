package com.digcoin.snapx.domain.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digcoin.snapx.domain.member.entity.MemberEventAggregation;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

public interface MemberEventAggregationMapper extends BaseMapper<MemberEventAggregation> {

    Long getEventSum(@Param("event") String event, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    IPage<MemberEventAggregation> pageEventPerMonth(IPage<MemberEventAggregation> pager, @Param("event") String event, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    IPage<MemberEventAggregation> pageEventPerDay(IPage<MemberEventAggregation> pager, @Param("event") String event, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    IPage<MemberEventAggregation> pageEventPerHour(IPage<MemberEventAggregation> pager, @Param("event") String event, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}
