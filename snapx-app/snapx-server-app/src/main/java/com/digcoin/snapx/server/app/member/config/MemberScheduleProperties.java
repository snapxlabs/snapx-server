package com.digcoin.snapx.server.app.member.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.DayOfWeek;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "app.push.schedule.member")
public class MemberScheduleProperties {

    private Set<DayOfWeek> targetWeekDay;
    private Integer targetHours;

}
