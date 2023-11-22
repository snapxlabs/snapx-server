package com.digcoin.snapx.core.mybatis.helper;

import com.digcoin.snapx.core.mybatis.RangeQuery;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DateRangeHelper {

    /**
     * 调整前端输入查询日期，适配为半开区间查询条件，查询结果包含开始日期，不包含结束日期
     * @param rangeQuery
     * @return
     */
    public static RangeQuery<Date> getDateRange(RangeQuery<Date> rangeQuery) {
        Date starting = rangeQuery.getStarting();
        Date ending = rangeQuery.getEnding();

        // 开始日期的时分秒毫秒置0
        if (Objects.nonNull(starting)) {
            starting = setTimeToZero(starting);
        }

        // 结束日期的时分秒毫秒置0，天数加1
        if (Objects.nonNull(ending)) {
            ending = getNextDayAndSetTimeToZero(ending);
        }

        return new DateRange(starting, ending);
    }

    /**
     * 将日期的时分秒，毫秒置0
     * @param date
     * @return
     */
    public static Date setTimeToZero(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 日期加1天，时分秒，毫秒置0
     * @param date
     * @return
     */
    public static Date getNextDayAndSetTimeToZero(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    static class DateRange implements RangeQuery<Date> {

        private final Date startDate;

        private final Date endDate;

        public DateRange(Date startDate, Date endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        public Date getStarting() {
            return startDate;
        }

        @Override
        public Date getEnding() {
            return endDate;
        }
    }

}
