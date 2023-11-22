package com.digcoin.snapx.server.app.restaurant.converter;

import com.digcoin.snapx.domain.restaurant.entity.Order;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import com.digcoin.snapx.server.app.restaurant.dto.command.OrderCreateOrderCmd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/28 16:01
 * @description
 */
@Mapper(componentModel = "spring")
public interface AppOrderServiceConverter {

    @Mapping(source = "latLng.lat", target = "lat")
    @Mapping(source = "latLng.lng", target = "lng")
    Order fromDTO(OrderCreateOrderCmd cmd);

    @Mapping(source = "spend", target = "spent")
    @Mapping(source = "rating", target = "rate")
    RestaurantReview dto2review(OrderCreateOrderCmd cmd);

}
