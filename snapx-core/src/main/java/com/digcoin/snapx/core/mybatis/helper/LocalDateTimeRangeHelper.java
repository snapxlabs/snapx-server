package com.digcoin.snapx.core.mybatis.helper;

import com.digcoin.snapx.core.mybatis.RangeQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * @author gengyang.chen
 * @version 1.0.0
 * @ClassName LocalDateTimeRangeHelper.java
 * @Description
 * @createTime 2021/6/25 9:39 上午
 */
public class LocalDateTimeRangeHelper {

    /**
     * 调整前端输入查询日期，适配为半开区间查询条件，查询结果包含开始日期，不包含结束日期
     * @param rangeQuery
     * @return
     */
    public static RangeQuery<LocalDateTime> getLocalDateTimeRange(RangeQuery<LocalDateTime> rangeQuery) {
        LocalDateTime starting = rangeQuery.getStarting();
        LocalDateTime ending = rangeQuery.getEnding();

        if (Objects.nonNull(starting)) {
            starting = setTimeToZero(starting);
        }

        if (Objects.nonNull(ending)) {
            ending = ending.plusDays(1);
            ending = setTimeToZero(ending);
        }

        return new LocalDateTimeRange(starting, ending);
    }

    /**
     * 将日期的时分秒，毫秒置0
     * @param localDateTime
     * @return
     */
    public static LocalDateTime setTimeToZero(LocalDateTime localDateTime) {
        LocalDate date = LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth());
        LocalTime time = LocalTime.of(0, 0);
        return LocalDateTime.of(date, time);
    }

    static class LocalDateTimeRange implements RangeQuery<LocalDateTime> {

        private final LocalDateTime startDate;

        private final LocalDateTime endDate;

        public LocalDateTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        public LocalDateTime getStarting() {
            return startDate;
        }

        @Override
        public LocalDateTime getEnding() {
            return endDate;
        }
    }

}
