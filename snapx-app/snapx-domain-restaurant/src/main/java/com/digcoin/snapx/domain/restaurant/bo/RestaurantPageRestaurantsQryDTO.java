package com.digcoin.snapx.domain.restaurant.bo;

import com.digcoin.snapx.core.dto.SortParamDTO;
import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:33
 * @description
 */
@Data
public class RestaurantPageRestaurantsQryDTO extends Pageable implements Serializable {

    @Length(max = 100, message = "")
    @Schema(description = "关键词", defaultValue = "")
    private String keyword;

    @Schema(description = "地点 ID")
    private Long localityId;

    @Schema(description = "是否只显示系统餐厅")
    private Boolean onlyInside;

    @Schema(description = "排序：popular=按人气（默认按最多评价）；rate=按评分")
    private List<SortParamDTO> orderBy;

    @Getter
    public enum OrderByField {

        POPULAR("popular", "按人气（默认按最多评价）"),

        RATE("rate", "按评分");

        private final String field;

        private final String desc;

        OrderByField(String field, String desc) {
            this.field = field;
            this.desc = desc;
        }
    }

}
