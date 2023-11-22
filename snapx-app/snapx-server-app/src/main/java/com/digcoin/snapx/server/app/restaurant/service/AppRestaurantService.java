package com.digcoin.snapx.server.app.restaurant.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.infra.bo.LocationBO;
import com.digcoin.snapx.domain.infra.bo.ResourceFileBo;
import com.digcoin.snapx.domain.infra.component.googlemap.GeoApiContextFactory;
import com.digcoin.snapx.domain.infra.service.LocationService;
import com.digcoin.snapx.domain.infra.service.ResourceFileService;
import com.digcoin.snapx.domain.restaurant.bo.*;
import com.digcoin.snapx.domain.restaurant.constant.ExecutorNameConst;
import com.digcoin.snapx.domain.restaurant.entity.Restaurant;
import com.digcoin.snapx.domain.restaurant.event.RestaurantCreatedEvent;
import com.digcoin.snapx.domain.restaurant.service.RestaurantReviewService;
import com.digcoin.snapx.domain.restaurant.service.RestaurantService;
import com.digcoin.snapx.server.app.restaurant.converter.AppRestaurantConverter;
import com.digcoin.snapx.server.app.restaurant.dto.RestaurantDTO;
import com.digcoin.snapx.server.app.restaurant.dto.command.RestaurantCreateRestaurantCmd;
import com.digcoin.snapx.server.app.restaurant.dto.command.RestaurantRetryStorageRstPhotoCmd;
import com.digcoin.snapx.server.app.restaurant.dto.command.query.RestaurantFindRestaurantQry;
import com.digcoin.snapx.server.app.restaurant.vo.RstPageGoogleNearbyVO;
import com.digcoin.snapx.server.base.member.service.BaseGiftCountHandleService;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.google.maps.ImageResult;
import com.google.maps.PhotoRequest;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/28 10:06
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppRestaurantService {

    private final static String KEY_PREFIX = "SNAPX:APP:RESTAURANT";

    private final RestaurantService restaurantService;
    private final RestaurantReviewService restaurantReviewService;
    private final AppRestaurantConverter appRestaurantConverter;
    private final LocationService locationService;
    private final RedissonClient redissonClient;
    private final ResourceFileService resourceFileService;
    private final ApplicationContext applicationContext;
    private final GeoApiContextFactory geoApiContextFactory;
    private final StringRedisTemplate redisTemplate;
    private final BaseGiftCountHandleService baseGiftCountHandleService;
    private final AppRestaurantAggregationService appRestaurantAggregationService;

    @Transactional(rollbackFor = Exception.class)
    public RestaurantDTO createRestaurant(RestaurantCreateRestaurantCmd cmd) {
        LocationBO location = locationService.getSystemLocationByLatLng(cmd.getLatLng().getLat(), cmd.getLatLng().getLng());
        Restaurant restaurant = appRestaurantConverter.fromDTO(cmd);
        restaurant.setCountryId(location.getCountry().getId());
        restaurant.setLocalityId(location.getLocality().getId());
        restaurantService.createRestaurantOrFail(restaurant);
        return appRestaurantConverter.restaurant2DTO(restaurant);
    }

    public PageResult<RestaurantDTO> pageRestaurants(RestaurantPageRestaurantsQryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<Restaurant> list = restaurantService.listRestaurants(dto);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.emptyResult();
        }

        Map<Long, RestaurantDTO> restaurantDTOMap = appRestaurantAggregationService.mappingRestaurantDTO(list, null);
        List<RestaurantDTO> dtoList = list.stream().map(
                item -> restaurantDTOMap.get(item.getId())).collect(Collectors.toList());

        return PageResult.converter(list, dtoList);
    }

    public PageResult<RestaurantDTO> pageNearbyRestaurants(RestaurantPageNearbyRestaurantsQryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<RestaurantBO> list = restaurantService.listGoogleNearbyRestaurants(dto.getLocalityId(),
                false, dto.getLat(), dto.getLng(), dto.getRadius(), dto.getKeyword());
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.emptyResult();
        }

        Map<Long, RestaurantDTO> restaurantDTOMap = appRestaurantAggregationService.mappingRestaurantDTO2(list,
                new LatLngDTO(dto.getLng(), dto.getLat()));
        List<RestaurantDTO> dtoList = list.stream().map(
                item -> restaurantDTOMap.get(item.getId())).collect(Collectors.toList());

        return PageResult.converter(list, dtoList);
    }

    public RstPageGoogleNearbyVO pageGoogleNearbyRestaurants(RestaurantPageGoogleNearbyRestaurantsQryDTO dto) {
        RstPageGoogleNearbyVO vo = new RstPageGoogleNearbyVO();

        // 分页处理
        String nextPageToken = dto.getNextPageToken();
        String nextPageTokenZhHk;

        // 从缓存中拿出英文分页对应的繁体分页
        String keyPrefix = String.format("%s:%s", KEY_PREFIX, "GOOGLE:NEARBY:RESTAURANT:NEXT_PAGE_TOKEN");
        String key = Joiner.on(":").skipNulls().join(keyPrefix, nextPageToken);
        nextPageTokenZhHk = redisTemplate.opsForValue().get(key);

        // 查英文餐厅
        LatLng latLng = new LatLng(dto.getLat().doubleValue(), dto.getLng().doubleValue());
        PlacesSearchResponse response = restaurantService.getGoogleNearbyRestaurants(latLng, dto.getRadius(),
                geoApiContextFactory.getDefault(), nextPageToken, 0);
        List<PlacesSearchResult> list = new ArrayList<>();
        if (Objects.nonNull(response)) {
            list = Arrays.asList(response.results);
            nextPageToken = response.nextPageToken;
        }

        // 查繁体餐厅
        PlacesSearchResponse responseZhHk = restaurantService.getGoogleNearbyRestaurants(latLng, dto.getRadius(),
                geoApiContextFactory.get(GeoApiContextFactory.LANGUAGE_ZH_HK), nextPageTokenZhHk, 1);
        List<PlacesSearchResult> listZhHk = new ArrayList<>();
        if (Objects.nonNull(responseZhHk)) {
            listZhHk = Arrays.asList(responseZhHk.results);
            nextPageTokenZhHk = responseZhHk.nextPageToken;
        }

        // 映射餐厅名称
        Map<String, String> nameMap = new HashMap<>(16);
        for (PlacesSearchResult result : listZhHk) {
            nameMap.put(result.placeId, result.name);
        }

        if (CollectionUtils.isEmpty(list)) {
            return vo;
        }

        // 转换
        List<RestaurantDTO> restaurantDTOs = appRestaurantConverter.toDTO(list);
        restaurantDTOs.stream().map(item -> {
            item.setGoogle(true);
            appRestaurantConverter.calcDistance(dto.getLat(), dto.getLng(), item);
            String nameZhHk = nameMap.get(item.getPlaceId());
            item.setNameZhHk(nameZhHk);
            return item;
        }).collect(Collectors.toList());

        if (StringUtils.isNoneBlank(nextPageToken)) {
            key = Joiner.on(":").skipNulls().join(keyPrefix, nextPageToken);
            redisTemplate.opsForValue().set(key, nextPageTokenZhHk, 60L, TimeUnit.MINUTES);
            vo.setNextPageToken(nextPageToken);
        }

        vo.setList(restaurantDTOs);
        return vo;
    }

    public RestaurantDTO findRestaurant(RestaurantFindRestaurantQry qry) {
        Restaurant restaurant = restaurantService.findByIdOrFail(qry.getRestaurantId());
        restaurantService.visit(qry.getRestaurantId());
        Map<Long, RestaurantDTO> restaurantDTOMap = appRestaurantAggregationService.mappingRestaurantDTO(Arrays.asList(restaurant), null);
        RestaurantDTO dto = restaurantDTOMap.get(qry.getRestaurantId());
        return dto;
    }

    @EventListener
    public void handleRestaurantCreatedEvent(RestaurantCreatedEvent event) {
        log.info("receive handle-restaurant-created-event: {}", event);

        AppRestaurantService service = applicationContext.getBean(AppRestaurantService.class);
        service.storageRstPhotoExecutor(event.getRestaurantId());
    }

    @Async(ExecutorNameConst.STORAGE_RST_PHOTO_EXECUTOR)
    public void storageRstPhotoExecutor(Long restaurantId) {
        log.info("storage-rst-photo-executor: name={}, restaurantId={}", Thread.currentThread().getName(), restaurantId);

        ThreadPoolTaskExecutor taskExecutor = applicationContext.getBean(ExecutorNameConst.STORAGE_RST_PHOTO_EXECUTOR, ThreadPoolTaskExecutor.class);
        int currentThreads = taskExecutor.getThreadPoolExecutor().getActiveCount();
        log.info("Current active threads: {}", currentThreads);

        String key = Joiner.on(":").join("storage-rst-photo-executor", restaurantId);
        RLock lock = redissonClient.getLock(key);
        if (!lock.tryLock()) {
            return;
        }

        try {
            Restaurant restaurant = restaurantService.findByIdOrFail(restaurantId);
            if (StringUtils.isNoneBlank(restaurant.getCoverUrl())) {
                log.info("the restaurant(id={}) already exists coverUrl", restaurantId);
                return;
            }
            if (!restaurant.getGoogle() || StringUtils.isBlank(restaurant.getPhotoReference())) {
                return;
            }

            try {
                PhotoRequest photoRequest = new PhotoRequest(geoApiContextFactory.getDefault()).photoReference(restaurant.getPhotoReference()).maxWidth(restaurant.getPhotoWidth());

                ImageResult imageResult = photoRequest.await();

                List<String> list = Arrays.asList(imageResult.contentType.split("/"));
                String suffix = !list.isEmpty() ? list.get(list.size() - 1) : "";
                String filename = String.format("%s.%s", restaurantId, suffix);

                ResourceFileBo fileBo = ResourceFileBo.fromBytes(imageResult.imageData, filename, suffix, imageResult.contentType);
                Long fileId = resourceFileService.createResourceFile(fileBo);
                fileBo = resourceFileService.findResourceFile(fileId);
                restaurantService.updateCover(restaurant, fileBo.getFileUrl());

                log.error("storage-rst-photo-success: restaurantId={}", restaurantId);
            } catch (Exception e) {
                log.error("storage-rst-photo-error: restaurantId={}, message={}", e.getMessage());
                e.printStackTrace();
                // restaurantService.publishDelayRestaurantCreatedEvent(restaurantId);
            }

        } finally {
            lock.unlock();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void retryStorageRstPhoto(RestaurantRetryStorageRstPhotoCmd cmd) {
        if (CollectionUtils.isEmpty(cmd.getIds())) {
            return;
        }
        for (Long id : cmd.getIds()) {
            restaurantService.publishRestaurantCreatedEvent(id);
        }
    }

}
