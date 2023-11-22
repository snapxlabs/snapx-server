package com.digcoin.snapx.domain.restaurant.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/2 20:10
 * @description
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewOverviewDTO {

    private BigDecimal avgEarned;
    private BigDecimal avgSpentUsd;

    private Long rating1Num;
    private Long rating2Num;
    private Long rating3Num;
    private Long rating4Num;
    private Long rating5Num;

    public Long getRatingNumSum() {
        return rating1Num + rating2Num + rating3Num + rating4Num + rating5Num;
    }

    public BigDecimal countAvgRating() {
        long sum = 1 * rating1Num
                + 2 * rating2Num
                + 3 * rating3Num
                + 4 * rating4Num
                + 5 * rating5Num;

        if (sum == 0 || getRatingNumSum() == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal avgRating = BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(getRatingNumSum()), 2, RoundingMode.HALF_UP);
        return avgRating;
    }


}
