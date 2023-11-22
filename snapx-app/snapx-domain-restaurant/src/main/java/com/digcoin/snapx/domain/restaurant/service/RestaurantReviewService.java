package com.digcoin.snapx.domain.restaurant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.enums.SortDirection;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.dto.SortParamDTO;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.core.redisson.BlockingQueueManager;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.restaurant.bo.RestaurantReviewPageReviewsQryDTO;
import com.digcoin.snapx.domain.restaurant.bo.ReviewOverviewDTO;
import com.digcoin.snapx.domain.restaurant.constant.QueueConst;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewLikes;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewPhoto;
import com.digcoin.snapx.domain.restaurant.event.RestaurantReviewLikedEvent;
import com.digcoin.snapx.domain.restaurant.event.RestaurantReviewedEvent;
import com.digcoin.snapx.domain.restaurant.mapper.RestaurantReviewLikesMapper;
import com.digcoin.snapx.domain.restaurant.mapper.RestaurantReviewMapper;
import com.digcoin.snapx.domain.restaurant.mapper.RestaurantReviewPhotoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/28 10:30
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantReviewService {

    private final RestaurantReviewMapper restaurantReviewMapper;
    private final RestaurantReviewPhotoMapper restaurantReviewPhotoMapper;
    private final RestaurantReviewLikesMapper restaurantReviewLikesMapper;

    @Qualifier(QueueConst.RESTAURANT_REVIEW_LIKED_EVENT_QUEUE)
    private final BlockingQueueManager<RestaurantReviewLikedEvent> restaurantReviewLikedEventQueue;

    @Qualifier(QueueConst.RESTAURANT_REVIEWED_QUEUE)
    private final BlockingQueueManager<RestaurantReviewedEvent> restaurantReviewedEventQueue;

    public RestaurantReview findByIdOrFail(Long id) {
        RestaurantReview review = restaurantReviewMapper.selectById(id);
        if (Objects.isNull(review)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("ReviewId not found");
        }
        return review;
    }

    public PageResult<RestaurantReview> pageReviews(RestaurantReviewPageReviewsQryDTO dto, CurrentUser currentUser) {
        LambdaQueryWrapper<RestaurantReview> queryWrapper = Wrappers.<RestaurantReview>lambdaQuery()
                .and(wrapper -> {
                    wrapper.eq(RestaurantReview::getRestaurantId, Optional.ofNullable(dto.getRestaurantId()).orElse(0L));
                    wrapper.or();
                    wrapper.eq(RestaurantReview::getLocalityId, Optional.ofNullable(dto.getLocalityId()).orElse(0L));
                })
                .orderByDesc(RestaurantReview::getCreateTime);

        IPage<RestaurantReview> pageResult = restaurantReviewMapper.selectPage(PageHelper.getPage(dto), queryWrapper);
        return PageResult.fromPage(pageResult, Function.identity());
    }

    public List<RestaurantReview> listByRestaurantIdOrLocalityId(Long restaurantId, Long localityId, List<Integer> ratings, List<SortParamDTO> orderBy) {
        LambdaQueryWrapper<RestaurantReview> queryWrapper = Wrappers.<RestaurantReview>lambdaQuery();

        // 查指定餐厅
        if (Objects.nonNull(restaurantId)) {
            queryWrapper.eq(RestaurantReview::getRestaurantId, restaurantId);
        }
        // 查指定城市
        if (Objects.nonNull(localityId)) {
            queryWrapper.eq(RestaurantReview::getLocalityId, localityId);
        }
        // 评分
        if (Objects.nonNull(ratings)) {
            ratings = !ratings.isEmpty() ? ratings : Arrays.asList(-1);
            queryWrapper.in(RestaurantReview::getRate, ratings);
        }

        // 自定义排序
        orderBy = Optional.ofNullable(orderBy).orElse(Collections.emptyList());
        for (SortParamDTO sortParamDTO : orderBy) {
            // 按人气
            if (RestaurantReviewPageReviewsQryDTO.OrderByField.LATEST.getField().equals(sortParamDTO.getField())) {
                SortDirection direction = Optional.ofNullable(sortParamDTO.getDirection()).orElse(SortDirection.ASC);
                queryWrapper.orderBy(true, SortDirection.ASC.equals(direction), RestaurantReview::getCreateTime);
            }
            if (RestaurantReviewPageReviewsQryDTO.OrderByField.RATE.getField().equals(sortParamDTO.getField())) {
                // 按平均评分
                SortDirection direction = Optional.ofNullable(sortParamDTO.getDirection()).orElse(SortDirection.ASC);
                queryWrapper.orderBy(true, SortDirection.ASC.equals(direction), RestaurantReview::getRate);
            }
        }

        // 默认排序
        if (queryWrapper.getExpression().getOrderBy().isEmpty()) {
            queryWrapper.orderByDesc(RestaurantReview::getCreateTime);
        }

        return restaurantReviewMapper.selectList(queryWrapper);
    }

    public List<Long> listMemberIds(List<RestaurantReview> reviewList) {
        if (CollectionUtils.isEmpty(reviewList)) {
            return Collections.emptyList();
        }
        return reviewList.stream().map(RestaurantReview::getMemberId).collect(Collectors.toList());
    }

    public List<Long> listIds(List<RestaurantReview> reviewList) {
        if (CollectionUtils.isEmpty(reviewList)) {
            return Collections.emptyList();
        }
        return reviewList.stream().map(RestaurantReview::getId).collect(Collectors.toList());
    }

    public List<RestaurantReview> listByIds(List<Long> reviewIds) {
        return restaurantReviewMapper.selectList(Wrappers.<RestaurantReview>lambdaQuery()
                .in(RestaurantReview::getId, reviewIds)
                .orderByDesc(RestaurantReview::getCreateTime)
        );
    }

    public List<Long> listRestaurantIds(List<RestaurantReview> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(RestaurantReview::getRestaurantId).collect(Collectors.toList());
    }

    public Map<Long, RestaurantReview> mappingReviews(List<Long> reviewIds) {
        if (CollectionUtils.isEmpty(reviewIds)) {
            return Collections.emptyMap();
        }
        return listByIds(reviewIds).stream().collect(Collectors.toMap(RestaurantReview::getId, Function.identity()));
    }

    public void createReview(RestaurantReview review) {
        restaurantReviewMapper.insert(review);

        // 关联评价照片
        createReviewPhotos(review, review.getPhotoUrls());

        // 餐厅评价完成事件
        publishRestaurantReviewedEvent(review.getId());

    }

    public ReviewOverviewDTO countReviewOverview(Long restaurantId) {
        QueryWrapper<RestaurantReview> wrapper = new QueryWrapper<>();
        wrapper.eq(RestaurantReview.RESTAURANT_ID, restaurantId);
        wrapper.select(String.format("avg(%s) as avg_earned", RestaurantReview.EARNED),
                String.format("avg(%s) as avg_spent_usd", RestaurantReview.SPENT_USD),
                String.format("SUM(IF(%s = 1, 1, 0)) AS rating1_num, SUM(IF(%s = 2, 1, 0)) AS rating2_num, SUM(IF(%s = 3, 1, 0)) AS rating3_num, SUM(IF(%s = 4, 1, 0)) AS rating4_num, SUM(IF(%s = 5, 1, 0)) AS rating5_num", RestaurantReview.RATE, RestaurantReview.RATE, RestaurantReview.RATE, RestaurantReview.RATE, RestaurantReview.RATE));
        List<Map<String, Object>> maps = restaurantReviewMapper.selectMaps(wrapper);
        System.out.println(maps);

        if (CollectionUtils.isEmpty(maps)) {
            return null;
        }

        Map<String, Object> map = maps.get(0);
        BigDecimal avgEarned = BigDecimal.ZERO;
        BigDecimal avgSpentUsd = BigDecimal.ZERO;
        Long rating1Num = 0L;
        Long rating2Num = 0L;
        Long rating3Num = 0L;
        Long rating4Num = 0L;
        Long rating5Num = 0L;
        if (Objects.nonNull(map)) {
            avgEarned = (BigDecimal) map.getOrDefault("avg_earned", BigDecimal.ZERO);
            avgSpentUsd = (BigDecimal) map.getOrDefault("avg_spent_usd", BigDecimal.ZERO);
            rating1Num = ((BigDecimal) map.getOrDefault("rating1_num", BigDecimal.ZERO)).longValue();
            rating2Num = ((BigDecimal) map.getOrDefault("rating2_num", BigDecimal.ZERO)).longValue();
            rating3Num = ((BigDecimal) map.getOrDefault("rating3_num", BigDecimal.ZERO)).longValue();
            rating4Num = ((BigDecimal) map.getOrDefault("rating4_num", BigDecimal.ZERO)).longValue();
            rating5Num = ((BigDecimal) map.getOrDefault("rating5_num", BigDecimal.ZERO)).longValue();
        }

        return new ReviewOverviewDTO(avgEarned, avgSpentUsd, rating1Num, rating2Num, rating3Num, rating4Num, rating5Num);
    }

    public void createReviewPhotos(RestaurantReview review, String photoUrls) {
        if (StringUtils.isBlank(photoUrls)) {
            throw CommonError.PARAMETER_MISS_ERROR.withMessage("Photos cannot be empty");
        }

        List<RestaurantReviewPhoto> restaurantReviewPhotos = new ArrayList<>();
        List<String> urls = Arrays.asList(photoUrls.split(","));
        for (String url : urls) {
            RestaurantReviewPhoto photo = new RestaurantReviewPhoto();
            BeanUtils.copyProperties(review, photo);
            photo.setId(null);
            photo.setReviewId(review.getId());
            photo.setPhotoUrl(url);
            photo.setFileId(0L);
            restaurantReviewPhotoMapper.insert(photo);
            restaurantReviewPhotos.add(photo);
        }
    }

    public RestaurantReview findReviewByIdOrFail(Long reviewId) {
        RestaurantReview review = restaurantReviewMapper.selectById(reviewId);
        if (Objects.isNull(review)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("ReviewId not found");
        }
        return review;
    }

    public Long updateLikesNum(Long reviewId) {
        Long count = restaurantReviewLikesMapper.selectCount(Wrappers.<RestaurantReviewLikes>lambdaQuery().eq(RestaurantReviewLikes::getReviewId, reviewId));

        LambdaUpdateWrapper<RestaurantReview> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RestaurantReview::getId, reviewId);
        updateWrapper.set(RestaurantReview::getLikesNum, count);
        restaurantReviewMapper.update(null, updateWrapper);
        return count;
    }

    public void publishRestaurantReviewedEvent(Long reviewId) {
        // 在事务上下文中注册 TransactionSynchronization 对象
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                // 在事务提交后发布消息
                restaurantReviewedEventQueue.publishEvent(new RestaurantReviewedEvent(reviewId));
            }
        });
    }

    public void publishRestaurantReviewLikedEvent(Long reviewId, Boolean likes, Long fromMemberId) {
        restaurantReviewLikedEventQueue.publishEvent(new RestaurantReviewLikedEvent(reviewId, likes, fromMemberId));
    }

    public Map<Long, Long> mappingMemberPhotosNum(List<Long> memberIds) {
        if (CollectionUtils.isEmpty(memberIds)) {
            return Collections.emptyMap();
        }

        memberIds = memberIds.stream().distinct().collect(Collectors.toList());

        QueryWrapper<RestaurantReviewPhoto> wrapper = new QueryWrapper<>();
        wrapper.in(RestaurantReviewPhoto.MEMBER_ID, memberIds);
        wrapper.groupBy(RestaurantReviewPhoto.MEMBER_ID);
        wrapper.select(String.format("`%s`, COUNT(1) as count", RestaurantReviewPhoto.MEMBER_ID));
        List<Map<String, Object>> list = restaurantReviewPhotoMapper.selectMaps(wrapper);

        Map<Long, Long> resultMap = new HashMap<>(16);
        for (Map<String, Object> map : list) {
            BigInteger memberId = (BigInteger) map.get(RestaurantReviewPhoto.MEMBER_ID);
            Long count = (Long) map.getOrDefault("count", 0L);
            resultMap.put(memberId.longValue(), count);
        }

        for (Long memberId : memberIds) {
            if (Objects.isNull(resultMap.get(memberId))) {
                resultMap.put(memberId, 0L);
            }
        }

        return resultMap;
    }

    public Long getTotalPhotosCount() {
        return restaurantReviewPhotoMapper.selectCount(Wrappers.lambdaQuery(RestaurantReviewPhoto.class));
    }

    public void delete(Long id) {
        restaurantReviewMapper.deleteById(id);
        deletePhotosByReviewId(id);
    }

    public boolean deletePhotosByReviewId(Long reviewId) {
        return restaurantReviewPhotoMapper.delete(Wrappers.<RestaurantReviewPhoto>lambdaQuery().eq(RestaurantReviewPhoto::getReviewId, reviewId)) > 0;
    }

    public void updateEarned(RestaurantReview review, BigDecimal earned) {
        review.setEarned(earned);
        restaurantReviewMapper.updateById(review);
    }

    public boolean allowUpdate(RestaurantReview review, Long currMemberId) {
        long days = Duration.between(LocalDateTime.now(), review.getCreateTime()).toDays();
        return Objects.equals(review.getMemberId(), currMemberId) && days <= 10;
    }

    public Map<Long, Boolean> mappingAllowUpdate(List<RestaurantReview> reviews, Long currMemberId) {
        if (CollectionUtils.isEmpty(reviews)) {
            return Collections.emptyMap();
        }

        Map<Long, Boolean> map = new HashMap<>(16);
        for (RestaurantReview review : reviews) {
            map.put(review.getId(), allowUpdate(review, currMemberId));
        }

        return map;
    }

    public void updateRateAndContent(RestaurantReview review, Integer rating, String content) {
        review.setRate(rating);
        review.setContent(content);
        restaurantReviewMapper.updateById(review);
    }


    public Map<Long, List<RestaurantReview>> listByRestaurantIds(Set<Long> restaurantIds) {
        return restaurantReviewMapper.selectList(Wrappers.<RestaurantReview>lambdaQuery()
                        .in(RestaurantReview::getRestaurantId, restaurantIds)).stream()
                .collect(Collectors.groupingBy(RestaurantReview::getRestaurantId));
    }

    public int updateNewLocalityId(Long oldLocalityId, Long newLocalityId) {
        List<RestaurantReview> reviews = listByLocality(oldLocalityId);
        if (reviews.isEmpty()) {
            return 0;
        }

        LambdaUpdateWrapper<RestaurantReview> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RestaurantReview::getLocalityId, oldLocalityId);
        updateWrapper.set(RestaurantReview::getLocalityId, newLocalityId);
        return restaurantReviewMapper.update(null, updateWrapper);
    }

    public List<RestaurantReview> listByLocality(Long localityId) {
        if (Objects.isNull(localityId)) {
            return Collections.emptyList();
        }
        return restaurantReviewMapper.selectList(Wrappers.<RestaurantReview>lambdaQuery().eq(RestaurantReview::getLocalityId, localityId));
    }

    public Long countByRestaurantId(Long restaurantId) {
        return restaurantReviewMapper.selectCount(Wrappers.<RestaurantReview>lambdaQuery()
                .eq(RestaurantReview::getRestaurantId, restaurantId)
        );
    }
}
