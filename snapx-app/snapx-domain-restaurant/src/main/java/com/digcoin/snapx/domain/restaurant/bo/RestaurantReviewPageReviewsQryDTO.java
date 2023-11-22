package com.digcoin.snapx.domain.restaurant.bo;

import com.digcoin.snapx.core.dto.SortParamDTO;
import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:33
 * @description
 */
@Data
public class RestaurantReviewPageReviewsQryDTO extends Pageable implements Serializable {

    @Schema(description = "餐厅 ID")
    private Long restaurantId;

    @Schema(description = "地点 ID")
    private Long localityId;

    @Schema(description = "排序：latest=按时间（默认最新）；rate=按评分")
    private List<SortParamDTO> orderBy;

    @Schema(description = "评分数组：1-5")
    private List<Integer> ratings;

    @Getter
    public enum OrderByField {

        LATEST("latest", "按时间（默认按最新评价）"),

        RATE("rate", "按评分");

        private final String field;

        private final String desc;

        OrderByField(String field, String desc) {
            this.field = field;
            this.desc = desc;
        }
    }

}
