package com.digcoin.snapx.server.app.restaurant.vo;

import com.digcoin.snapx.server.app.restaurant.dto.RestaurantReviewDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:43
 * @description
 */
@Data
public class JournalPageJournalsVO {

    @Schema(description = "已赚")
    private BigDecimal earned;

    @Schema(description = "收入单位")
    private String earnedUnit;

    @Schema(description = "评价")
    private RestaurantReviewDTO review;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

}
