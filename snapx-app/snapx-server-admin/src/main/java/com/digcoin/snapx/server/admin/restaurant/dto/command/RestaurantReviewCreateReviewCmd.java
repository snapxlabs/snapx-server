package com.digcoin.snapx.server.admin.restaurant.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/5/3 19:57
 * @description
 */
@Data
public class RestaurantReviewCreateReviewCmd {

    @NotNull(message = "countryId cannot be null")
    @Min(0)
    @Schema(description = "国家 ID")
    private Long countryId;

    @NotNull(message = "localityId cannot be null")
    @Min(value = 0, message = "")
    @Schema(description = "地点 ID")
    private Long localityId;

    @NotNull(message = "restaurantId cannot be null")
    @Min(value = 0, message = "")
    @Schema(description = "餐厅 ID")
    private Long restaurantId;

    @NotNull(message = "memberId cannot be null")
    @Min(value = 0, message = "")
    @Schema(description = "会员 ID")
    private Long memberId;

    @NotNull(message = "rate cannot be null")
    @Min(value = 1)
    @Max(value = 5)
    @Schema(description = "评分")
    private Integer rate;

    @NotNull(message = "spent cannot be null")
    @Schema(description = "花费")
    private BigDecimal spent;

    @NotNull(message = "currencyCode cannot be null")
    @Schema(description = "货币代码")
    private String currencyCode;

    @Length(max = 1000)
    @Schema(description = "内容")
    private String content;

    @Size(min = 1, max = 10, message = "Photos at least 1 and maximum of 10")
    @Schema(description = "图片")
    private List<String> photoUrls;

}
