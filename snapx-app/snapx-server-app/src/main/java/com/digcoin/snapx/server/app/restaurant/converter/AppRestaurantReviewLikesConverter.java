package com.digcoin.snapx.server.app.restaurant.converter;

import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewLikes;
import com.digcoin.snapx.server.app.restaurant.dto.LikesDTO;
import org.mapstruct.Mapper;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/18 16:05
 * @description
 */
@Mapper(componentModel = "spring")
public interface AppRestaurantReviewLikesConverter {

    LikesDTO toDTO(RestaurantReviewLikes entity);

}
