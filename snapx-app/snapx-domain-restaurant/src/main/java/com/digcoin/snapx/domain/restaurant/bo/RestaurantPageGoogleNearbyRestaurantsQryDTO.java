package com.digcoin.snapx.domain.restaurant.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/28 21:46
 * @description
 */
@ParameterObject
@Data
public class RestaurantPageGoogleNearbyRestaurantsQryDTO implements Serializable {

    @Schema(description = "经度")
    private BigDecimal lng;

    @Schema(description = "纬度")
    private BigDecimal lat;

    @Schema(description = "半径（默认 500M）")
    private BigDecimal radius;

    @Schema(description = "下一页 Token")
    private String nextPageToken;

}
