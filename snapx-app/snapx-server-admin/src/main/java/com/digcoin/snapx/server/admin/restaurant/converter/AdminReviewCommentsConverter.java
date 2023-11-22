package com.digcoin.snapx.server.admin.restaurant.converter;

import com.digcoin.snapx.domain.restaurant.bo.RestaurantReviewCommentsQueryBO;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewComments;
import com.digcoin.snapx.server.admin.restaurant.dto.AdminReviewCommentsPageDTO;
import com.digcoin.snapx.server.admin.restaurant.vo.AdminReviewCommentsVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/27 19:13
 * @description
 */
@Mapper(componentModel = "spring")
public interface AdminReviewCommentsConverter {

    RestaurantReviewCommentsQueryBO convertBO(AdminReviewCommentsPageDTO dto);

    AdminReviewCommentsVO intoVO(RestaurantReviewComments restaurantReviewComments);
}
