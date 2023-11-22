package com.digcoin.snapx.domain.restaurant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.enums.SortDirection;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.dto.SortParamDTO;
import com.digcoin.snapx.core.redisson.DelayMessageManager;
import com.digcoin.snapx.domain.restaurant.bo.RestaurantBO;
import com.digcoin.snapx.domain.restaurant.bo.RestaurantPageRestaurantsQryDTO;
import com.digcoin.snapx.domain.restaurant.bo.ReviewOverviewDTO;
import com.digcoin.snapx.domain.restaurant.constant.QueueConst;
import com.digcoin.snapx.domain.restaurant.constant.RadiusConst;
import com.digcoin.snapx.domain.restaurant.entity.Restaurant;
import com.digcoin.snapx.domain.restaurant.error.RestaurantError;
import com.digcoin.snapx.domain.restaurant.event.RestaurantCreatedEvent;
import com.digcoin.snapx.domain.restaurant.mapper.RestaurantMapper;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;

    @Qualifier(QueueConst.RESTAURANT_CREATED_EVENT_QUEUE)
    private final DelayMessageManager<RestaurantCreatedEvent> restaurantCreatedEventQueue;

    public List<Restaurant> listByLatlng(BigDecimal lat, BigDecimal lng) {
        return restaurantMapper.selectList(Wrappers.<Restaurant>lambdaQuery()
                .eq(Restaurant::getLat, lat)
                .eq(Restaurant::getLng, lng)
        );
    }

    public Boolean existsByLatlng(BigDecimal lat, BigDecimal lng) {
        return restaurantMapper.selectCount(Wrappers.<Restaurant>lambdaQuery()
                .eq(Restaurant::getLat, lat)
                .eq(Restaurant::getLng, lng)
        ) > 0;
    }

    public Restaurant findByIdOrFail(Long id) {
        Restaurant restaurant = restaurantMapper.selectById(id);
        if (Objects.isNull(restaurant)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("RestaurantId not found");
        }
        return restaurant;
    }

    public void createRestaurant(Restaurant restaurant) {
        restaurantMapper.insert(restaurant);

        // 创建餐厅事件
        publishRestaurantCreatedEvent(restaurant.getId());
    }

    public void createRestaurantOrFail(Restaurant restaurant) {
        if (existsByLatlng(restaurant.getLat(), restaurant.getLng())) {
            throw RestaurantError.RESTAURANT_ALREADY_EXISTS.withDefaults();
        }
        if (Objects.nonNull(restaurant.getPlaceId()) && existsByLPlaceId(restaurant.getPlaceId())) {
            throw RestaurantError.RESTAURANT_ALREADY_EXISTS.withDefaults();
        }
        createRestaurant(restaurant);
    }

    public boolean existsByLPlaceId(String placeId) {
        if (Objects.isNull(placeId)) {
            return false;
        }
        return restaurantMapper.selectCount(Wrappers.<Restaurant>lambdaQuery()
                .eq(Restaurant::getPlaceId, placeId)
        ) > 0;
    }

    public Restaurant findByLPlaceId(String placeId) {
        return restaurantMapper.selectOne(Wrappers.<Restaurant>lambdaQuery()
                .eq(Restaurant::getPlaceId, placeId)
        );
    }

    public Map<Long, Restaurant> mappingRestaurants(List<Long> restaurantIds) {
        if (CollectionUtils.isEmpty(restaurantIds)) {
            return Collections.emptyMap();
        }
        return listByIds(restaurantIds).stream().collect(Collectors.toMap(Restaurant::getId, Function.identity()));
    }

    public List<Restaurant> listByIds(List<Long> restaurantIds) {
        if (CollectionUtils.isEmpty(restaurantIds)) {
            return Collections.emptyList();
        }
        return restaurantMapper.selectList(Wrappers.<Restaurant>lambdaQuery()
                .in(Restaurant::getId, restaurantIds)
                .orderByDesc(Restaurant::getCreateTime)
        );
    }

    public void updateReviewOverview(Long restaurantId, ReviewOverviewDTO reviewOverviewDTO) {
        if (Objects.isNull(restaurantId) || Objects.isNull(reviewOverviewDTO)) {
            log.error("updateReviewOverview failed");
            return;
        }
        LambdaUpdateWrapper<Restaurant> updateWrapper = new LambdaUpdateWrapper<>();

        // where
        updateWrapper.eq(Restaurant::getId, restaurantId);

        // set avg_earned
        updateWrapper.set(Restaurant::getAvgEarned, reviewOverviewDTO.getAvgEarned());
        // set avg_spent_usd
        updateWrapper.set(Restaurant::getAvgSpentUsd, reviewOverviewDTO.getAvgSpentUsd());

        // set rating{x}_num
        updateWrapper.set(Restaurant::getRating1Num, reviewOverviewDTO.getRating1Num());
        updateWrapper.set(Restaurant::getRating2Num, reviewOverviewDTO.getRating2Num());
        updateWrapper.set(Restaurant::getRating3Num, reviewOverviewDTO.getRating3Num());
        updateWrapper.set(Restaurant::getRating4Num, reviewOverviewDTO.getRating4Num());
        updateWrapper.set(Restaurant::getRating5Num, reviewOverviewDTO.getRating5Num());

        // set avg_rating
        updateWrapper.set(Restaurant::getAvgRating, reviewOverviewDTO.countAvgRating());

        // set review_num
        updateWrapper.set(Restaurant::getReviewNum, reviewOverviewDTO.getRatingNumSum());

        restaurantMapper.update(null, updateWrapper);
    }

    public void updateReviewOverview(Restaurant restaurant, ReviewOverviewDTO reviewOverviewDTO) {
        updateReviewOverview(restaurant.getId(), reviewOverviewDTO);
    }

    public List<Restaurant> listRestaurants(RestaurantPageRestaurantsQryDTO dto) {
        LambdaQueryWrapper<Restaurant> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(dto.getKeyword())) {
            queryWrapper.and(nameQueryWrapper -> {
                nameQueryWrapper.like(Restaurant::getName, dto.getKeyword());
                nameQueryWrapper.or();
                nameQueryWrapper.like(Restaurant::getNameZhHk, ZhConverterUtil.toTraditional(dto.getKeyword()));
            });
        }
        if (Objects.nonNull(dto.getLocalityId())) {
            queryWrapper.eq(Restaurant::getLocalityId, dto.getLocalityId());
        }
        if (Objects.nonNull(dto.getOnlyInside()) && Objects.equals(dto.getOnlyInside(), true)) {
            queryWrapper.eq(Restaurant::getPlaceId, "");
        }
        queryWrapper.eq(Restaurant::getVerified, true);

        // 自定义排序
        List<SortParamDTO> orderBy = Optional.ofNullable(dto.getOrderBy()).orElse(Collections.emptyList());
        for (SortParamDTO sortParamDTO : orderBy) {
            // 按人气
            if (RestaurantPageRestaurantsQryDTO.OrderByField.POPULAR.getField().equals(sortParamDTO.getField())) {
                SortDirection direction = Optional.ofNullable(sortParamDTO.getDirection()).orElse(SortDirection.ASC);
                queryWrapper.orderBy(true, SortDirection.ASC.equals(direction), Restaurant::getReviewNum);
            }
            if (RestaurantPageRestaurantsQryDTO.OrderByField.RATE.getField().equals(sortParamDTO.getField())) {
                // 按平均评分
                SortDirection direction = Optional.ofNullable(sortParamDTO.getDirection()).orElse(SortDirection.ASC);
                queryWrapper.orderBy(true, SortDirection.ASC.equals(direction), Restaurant::getAvgRating);
            }
        }

        // 默认排序
        if (queryWrapper.getExpression().getOrderBy().isEmpty()) {
            queryWrapper.orderByDesc(Restaurant::getReviewNum);
        }

        return restaurantMapper.selectList(queryWrapper);
    }

    public List<RestaurantBO> listGoogleNearbyRestaurants(Long localityId, Boolean onlyInside, BigDecimal lat, BigDecimal lng, BigDecimal radius, String keyword) {
        lat = Optional.ofNullable(lat).orElse(BigDecimal.ZERO);
        lng = Optional.ofNullable(lng).orElse(BigDecimal.ZERO);
        radius = Optional.ofNullable(radius).orElse(RadiusConst.RADIUS_500M);
        return restaurantMapper.listNearbyRestaurants(localityId, onlyInside, lat, lng, radius, keyword);
    }

    public PlacesSearchResponse getGoogleNearbyRestaurants(LatLng latLng, BigDecimal radius, GeoApiContext geoApiContext, String nextPageToken, int i) {
        radius = Optional.ofNullable(radius).orElse(RadiusConst.RADIUS_500M);
        if (Objects.equals(nextPageToken, "null")) {
            nextPageToken = null;
        }

        PlacesSearchResponse response = null;
        try {
            if (StringUtils.isBlank(nextPageToken)) {
                response = PlacesApi.nearbySearchQuery(geoApiContext, latLng)
                        .radius(radius.multiply(new BigDecimal(1000)).intValue())
                        .type(PlaceType.RESTAURANT)
                        .await();
            } else {
                if (i == 0) {
                    Thread.sleep(1000);
                } else {
                    Thread.sleep(500);
                }
                response = PlacesApi.nearbySearchNextPage(geoApiContext, nextPageToken).await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public void publishRestaurantCreatedEvent(Long id) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                restaurantCreatedEventQueue.publishEvent(new RestaurantCreatedEvent(id), 1L, TimeUnit.SECONDS);
            }
        });
    }

    public void publishDelayRestaurantCreatedEvent(Long id) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                restaurantCreatedEventQueue.publishEvent(new RestaurantCreatedEvent(id), 10L, TimeUnit.MINUTES);
            }
        });
    }

    public void updateCover(Restaurant restaurant, String coverUrl) {
        restaurant.setCoverUrl(coverUrl);
        restaurantMapper.updateById(restaurant);
    }

    public boolean delete(Long id) {
        return restaurantMapper.deleteById(id) > 0;
    }

    public Boolean setSpecById(Long id, Boolean status) {
        Restaurant restaurant = findByIdOrFail(id);
        restaurant.setSpec(status);
        return restaurantMapper.updateById(restaurant) > 0;
    }

    public void updateRestaurant(Restaurant restaurant) {
        restaurantMapper.updateById(restaurant);
    }

    public void visit(Long restaurantId) {
        if (Objects.isNull(restaurantId)) {
            return;
        }
        restaurantMapper.increaseViews(restaurantId);
    }

    public int updateNewLocalityId(Long oldLocalityId, Long newLocalityId) {
        List<Restaurant> restaurants = listByLocality(oldLocalityId);
        if (restaurants.isEmpty()) {
            return 0;
        }

        LambdaUpdateWrapper<Restaurant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Restaurant::getLocalityId, oldLocalityId);
        updateWrapper.set(Restaurant::getLocalityId, newLocalityId);
        return restaurantMapper.update(null, updateWrapper);
    }

    public List<Restaurant> listByLocality(Long localityId) {
        if (Objects.isNull(localityId)) {
            return Collections.emptyList();
        }
        return restaurantMapper.selectList(Wrappers.<Restaurant>lambdaQuery().eq(Restaurant::getLocalityId, localityId));
    }

    public List<Restaurant> listByKeyword(String restaurantName) {
        return restaurantMapper.selectList(Wrappers.<Restaurant>lambdaQuery().and(nameQueryWrapper -> {
            nameQueryWrapper.like(Restaurant::getName, restaurantName);
            nameQueryWrapper.or();
            nameQueryWrapper.like(Restaurant::getNameZhHk, restaurantName);
        }));
    }

    public Boolean setVerifiedById(Long id, Boolean status) {
        Restaurant restaurant = findByIdOrFail(id);
        restaurant.setVerified(status);
        return restaurantMapper.updateById(restaurant) > 0;
    }

    public Boolean enableWatermark(Long id, Boolean status) {
        Restaurant restaurant = findByIdOrFail(id);
        restaurant.setWatermark(status);
        return restaurantMapper.updateById(restaurant) > 0;
    }
}
