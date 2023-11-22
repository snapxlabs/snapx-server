package com.digcoin.snapx.server.admin.restaurant.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/28 11:19
 * @description
 */
@Data
public class RestaurantReviewUpdateReviewCmd {

    @NotNull
    @Min(0)
    @Schema(description = "评价 ID")
    private Long id;

    @NotNull(message = "countryId cannot be null")
    @Min(0)
    @Schema(description = "国家 ID")
    private Long countryId;

    @NotNull(message = "localityId cannot be null")
    @Min(value = 0, message = "")
    @Schema(description = "地点 ID")
    private Long localityId;

    @NotNull(message = "rate cannot be null")
    @Min(value = 1)
    @Max(value = 5)
    @Schema(description = "评分")
    private Integer rate;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "图片")
    private List<String> photoUrls;

}
