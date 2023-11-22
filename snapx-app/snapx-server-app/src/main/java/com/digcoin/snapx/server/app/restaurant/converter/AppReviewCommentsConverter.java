package com.digcoin.snapx.server.app.restaurant.converter;

import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewComments;
import com.digcoin.snapx.server.app.restaurant.dto.RestaurantReviewCommentsDTO;
import com.digcoin.snapx.server.app.restaurant.vo.RestaurantReviewCommentsVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/24 23:35
 * @description
 */
@Mapper(componentModel = "spring")
public interface AppReviewCommentsConverter {

    RestaurantReviewComments intoDTO(RestaurantReviewCommentsDTO dto);

    RestaurantReviewCommentsVO intoVO (RestaurantReviewComments reviewComments);

}
