package com.digcoin.snapx.domain.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digcoin.snapx.domain.restaurant.bo.RestaurantBO;
import com.digcoin.snapx.domain.restaurant.entity.Restaurant;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 餐厅 Mapper 接口
 * </p>
 *
 * @author Generator
 * @since 2023-02-24
 */
public interface RestaurantMapper extends BaseMapper<Restaurant> {

    List<RestaurantBO> listNearbyRestaurants(Long localityId,
                                             Boolean onlyInside,
                                             BigDecimal lat, BigDecimal lng,
                                             BigDecimal radius,
                                             String keyword);

    void increaseViews(Long id);
}
