package com.digcoin.snapx.domain.restaurant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.constant.Common;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.restaurant.bo.RestaurantReviewCommentsQueryBO;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewComments;
import com.digcoin.snapx.domain.restaurant.mapper.RestaurantReviewCommentsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/24 23:16
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantReviewCommentsService {

    private final RestaurantReviewCommentsMapper restaurantReviewCommentsMapper;

    public Optional<RestaurantReviewComments> findById(Long id) {
        return Optional.ofNullable(restaurantReviewCommentsMapper.selectById(id));
    }

    public void createRestaurantReviewComments(RestaurantReviewComments restaurantReviewComments) {
        restaurantReviewCommentsMapper.insert(restaurantReviewComments);
    }

    public List<RestaurantReviewComments> listByReviewId(Long reviewId) {
        return restaurantReviewCommentsMapper.selectList(Wrappers.lambdaQuery(
                RestaurantReviewComments.class).eq(RestaurantReviewComments::getReviewId, reviewId)
                .orderByAsc(RestaurantReviewComments::getCreateTime));
    }

    public PageResult<RestaurantReviewComments> pageReviewComments(RestaurantReviewCommentsQueryBO queryBO) {
        QueryWrapper<RestaurantReviewComments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rrrc.deleted", Common.ZERO)
                .eq(Objects.nonNull(queryBO.getReviewId()), "rrrc.review_id", queryBO.getReviewId())
                .like(StringUtils.isNotBlank(queryBO.getFromMemberName()), "mm.nickname", queryBO.getFromMemberName())
                .like(StringUtils.isNotBlank(queryBO.getFromMemberAccount()), "mm.account", queryBO.getFromMemberAccount())
                .like(StringUtils.isNotBlank(queryBO.getContent()), "rrrc.content", queryBO.getContent())
                .last("order by rrrc.create_time desc ");
        return PageResult.fromPage(restaurantReviewCommentsMapper.pageReviewComments(PageHelper.getPage(queryBO), queryWrapper), Function.identity());
    }

    public void deleteReviewComments(Long id) {
        restaurantReviewCommentsMapper.deleteById(id);
    }

    public Map<Long, Long> getCommentsSumMapping(List<Long> reviewIds) {
        Map<Long, Long> result = new HashMap<>();
        restaurantReviewCommentsMapper.selectList(Wrappers.lambdaQuery(
                RestaurantReviewComments.class).in(RestaurantReviewComments::getReviewId, reviewIds))
                .stream().collect(Collectors.groupingBy(RestaurantReviewComments::getReviewId, Collectors.counting()))
                .forEach(result::put);
        return result;
    }
}
