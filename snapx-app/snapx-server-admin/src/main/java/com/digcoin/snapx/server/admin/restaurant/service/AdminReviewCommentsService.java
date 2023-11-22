package com.digcoin.snapx.server.admin.restaurant.service;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewComments;
import com.digcoin.snapx.domain.restaurant.service.RestaurantReviewCommentsService;
import com.digcoin.snapx.server.admin.restaurant.converter.AdminReviewCommentsConverter;
import com.digcoin.snapx.server.admin.restaurant.dto.AdminReviewCommentsPageDTO;
import com.digcoin.snapx.server.admin.restaurant.vo.AdminReviewCommentsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/27 19:06
 * @description
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AdminReviewCommentsService {

    private final RestaurantReviewCommentsService restaurantReviewCommentsService;

    private final AdminReviewCommentsConverter adminReviewCommentsConverter;

    public PageResult<AdminReviewCommentsVO> pageReviewComments(AdminReviewCommentsPageDTO dto) {
        PageResult<RestaurantReviewComments> pageResult = restaurantReviewCommentsService.pageReviewComments(adminReviewCommentsConverter.convertBO(dto));
        return PageResult.fromPageResult(pageResult, adminReviewCommentsConverter::intoVO);
    }

    public void deleteReviewComments(Long id) {
        restaurantReviewCommentsService.deleteReviewComments(id);
    }
}
