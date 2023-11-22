package com.digcoin.snapx.server.admin.restaurant.assembler;

import com.digcoin.snapx.core.dto.DTOAssembler;
import com.digcoin.snapx.domain.restaurant.entity.Restaurant;
import com.digcoin.snapx.server.admin.restaurant.dto.command.RestaurantCreateRestaurantCmd;
import com.digcoin.snapx.server.admin.restaurant.dto.command.RestaurantUpdateRestaurantCmd;
import com.digcoin.snapx.server.admin.restaurant.dto.RestaurantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/6 00:01
 * @description
 */
@Mapper(componentModel = "spring")
public interface BaseRestaurantAssembler extends DTOAssembler<RestaurantDTO, Restaurant> {

    @Mapping(source = "spec", target = "watermark")
    Restaurant toEntity(RestaurantCreateRestaurantCmd cmd);

    @Mapping(source = "spec", target = "watermark")
    Restaurant toEntity(RestaurantUpdateRestaurantCmd cmd);

}
