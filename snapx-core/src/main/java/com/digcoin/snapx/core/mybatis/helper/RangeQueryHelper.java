package com.digcoin.snapx.core.mybatis.helper;

import com.digcoin.snapx.core.mybatis.RangeQuery;

import java.time.LocalDateTime;
import java.util.Date;

public class RangeQueryHelper {

    public static RangeQuery<Date> getDateRange(RangeQuery<Date> rangeQuery) {
        return DateRangeHelper.getDateRange(rangeQuery);
    }

    public static RangeQuery<LocalDateTime> getLocalDateTimeRange(RangeQuery<LocalDateTime> rangeQuery) {
        return LocalDateTimeRangeHelper.getLocalDateTimeRange(rangeQuery);
    }

}
