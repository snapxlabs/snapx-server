package com.digcoin.snapx.domain.restaurant.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springdoc.api.annotations.ParameterObject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:33
 * @description
 */
@ParameterObject
@Data
public class RestaurantPageNearbyRestaurantsQryDTO extends Pageable implements Serializable {

    @Length(max = 100, message = "")
    @Schema(description = "关键词", defaultValue = "")
    private String keyword;

    @Schema(description = "地点 ID")
    private Long localityId;

    @Schema(description = "经度")
    private BigDecimal lng;

    @Schema(description = "纬度")
    private BigDecimal lat;

    @Schema(description = "半径，默认 20 KM（如果没有经纬度，调试时可设置到 50000，有一定数据，超出 50000 无法查 Google 餐厅）")
    private BigDecimal radius;

    @Deprecated
    @Schema(description = "是否只显示系统餐厅")
    private Boolean onlyInside;

}
