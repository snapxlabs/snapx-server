package com.digcoin.snapx.domain.restaurant.bo;

import com.digcoin.snapx.domain.restaurant.entity.Restaurant;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/3 09:09
 * @description
 */
@Data
public class RestaurantBO extends Restaurant {

    private BigDecimal distance;

}
