package com.digcoin.snapx.domain.restaurant.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewLikes;
import com.digcoin.snapx.domain.restaurant.mapper.RestaurantReviewLikesMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/28 13:51
 * @description
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantReviewLikesService {

    private final RestaurantReviewLikesMapper restaurantReviewLikesMapper;

    public Map<Long, Boolean> mappingMemberLikes(List<Long> reviewIds, Long memberId) {
        if (CollectionUtils.isEmpty(reviewIds) || Objects.isNull(memberId)) {
            return Collections.emptyMap();
        }

        Map<Long, Boolean> map = new LinkedHashMap<>();
        List<RestaurantReviewLikes> list = restaurantReviewLikesMapper.selectList(Wrappers.<RestaurantReviewLikes>lambdaQuery()
                .in(RestaurantReviewLikes::getReviewId, reviewIds)
                .eq(RestaurantReviewLikes::getMemberId, memberId)
        );
        Map<Long, RestaurantReviewLikes> likesMap = list.stream().collect(Collectors.toMap(RestaurantReviewLikes::getReviewId, Function.identity()));
        for (Long reviewId : reviewIds) {
            map.put(reviewId, Objects.nonNull(likesMap.get(reviewId)));
        }
        return map;
    }

    public Boolean like(RestaurantReview review, Boolean likes, Long memberId) {
        RestaurantReviewLikes reviewLikes = restaurantReviewLikesMapper.selectOne(Wrappers.<RestaurantReviewLikes>lambdaQuery()
                .eq(RestaurantReviewLikes::getReviewId, review.getId())
                .eq(RestaurantReviewLikes::getMemberId, memberId)
        );
        if (likes) {
            if (Objects.isNull(reviewLikes)) {
                reviewLikes = new RestaurantReviewLikes();
                reviewLikes.setReviewId(review.getId());
                reviewLikes.setMemberId(memberId);
                restaurantReviewLikesMapper.insert(reviewLikes);
            }
        } else {
            if (Objects.nonNull(reviewLikes)) {
                restaurantReviewLikesMapper.deleteById(reviewLikes);
            }
        }
        return likes;
    }

}
