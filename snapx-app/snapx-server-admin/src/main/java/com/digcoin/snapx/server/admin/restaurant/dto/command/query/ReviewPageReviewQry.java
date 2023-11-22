package com.digcoin.snapx.server.admin.restaurant.dto.command.query;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/8 13:34
 * @description
 */
@Data
public class ReviewPageReviewQry extends Pageable {

    @Schema(description = "评价 ID")
    private Long id;

    @Schema(description = "国家 ID")
    private Long countryId;

    @Schema(description = "地点 ID")
    private Long localityId;

    @Schema(description = "餐厅名称")
    private String restaurantName;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "评分")
    private List<Integer> rate;

}
