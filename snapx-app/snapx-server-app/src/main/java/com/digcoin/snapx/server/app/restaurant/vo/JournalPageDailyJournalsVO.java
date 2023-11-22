package com.digcoin.snapx.server.app.restaurant.vo;

import com.digcoin.snapx.server.app.restaurant.dto.RestaurantReviewDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:43
 * @description
 */
@Data
public class JournalPageDailyJournalsVO {

    @Schema(description = "日期")
    private LocalDate date;

    @Schema(description = "年")
    private Long year;

    @Schema(description = "月")
    private Long month;

    @Schema(description = "日")
    private Long day;

    @Schema(description = "今天日记总数")
    private Long totalNumber;

    @Schema(description = "今天总共已赚")
    private BigDecimal totalEarned;

    @Schema(description = "收入单位")
    private String totalEarnedUnit;

    @Schema(description = "今天评价列表")
    private List<RestaurantReviewDTO> reviews;

}
