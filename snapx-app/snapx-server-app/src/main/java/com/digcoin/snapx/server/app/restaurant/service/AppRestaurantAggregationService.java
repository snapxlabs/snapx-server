package com.digcoin.snapx.server.app.restaurant.service;

import com.digcoin.snapx.core.common.util.CustomCollectionUtil;
import com.digcoin.snapx.core.common.util.DistanceCalculator;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.infra.component.exchangerates.Symbols;
import com.digcoin.snapx.domain.infra.entity.Currency;
import com.digcoin.snapx.domain.infra.entity.GeoCountry;
import com.digcoin.snapx.domain.infra.service.CurrencyService;
import com.digcoin.snapx.domain.infra.service.GeoCountryService;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.service.MemberService;
import com.digcoin.snapx.domain.restaurant.bo.LatLngDTO;
import com.digcoin.snapx.domain.restaurant.bo.RestaurantBO;
import com.digcoin.snapx.domain.restaurant.entity.Restaurant;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import com.digcoin.snapx.domain.restaurant.service.*;
import com.digcoin.snapx.domain.system.constant.EarningCurrencyUnit;
import com.digcoin.snapx.server.app.restaurant.converter.AppRestaurantConverter;
import com.digcoin.snapx.server.app.restaurant.converter.AppRestaurantReviewConverter;
import com.digcoin.snapx.server.app.restaurant.dto.RestaurantDTO;
import com.digcoin.snapx.server.app.restaurant.dto.RestaurantReviewDTO;
import com.digcoin.snapx.server.base.infra.converter.BaseCurrencyConverter;
import com.digcoin.snapx.server.base.infra.service.BaseExchangeRatesService;
import com.digcoin.snapx.server.base.member.dto.BaseAvgGiftCountDTO;
import com.digcoin.snapx.server.base.member.dto.BaseGiftCountDTO;
import com.digcoin.snapx.server.base.member.service.BaseGiftCountHandleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/11 00:33
 * @description
 */
@Slf4j
@AllArgsConstructor
@Service
public class AppRestaurantAggregationService {

    private final RestaurantReviewService restaurantReviewService;
    private final RestaurantReviewLikesService restaurantReviewLikesService;
    private final RestaurantService restaurantService;
    private final AppRestaurantReviewConverter appRestaurantReviewConverter;
    private final MemberService memberService;
    private final AppRestaurantConverter appRestaurantConverter;
    private final BaseGiftCountHandleService baseGiftCountHandleService;
    private final RestaurantReviewChangeService restaurantReviewChangeService;
    private final RestaurantReviewCommentsService restaurantReviewCommentsService;
    private final GeoCountryService geoCountryService;
    private final CurrencyService currencyService;
    private final BaseCurrencyConverter baseCurrencyConverter;
    private final BaseExchangeRatesService baseExchangeRatesService;

    public Map<Long, RestaurantDTO> mappingRestaurantDTO2(List<RestaurantBO> restaurantsList, LatLngDTO latLng) {
        List<Restaurant> list = restaurantsList.stream().map(item -> {
            Restaurant restaurant = new Restaurant();
            BeanUtils.copyProperties(item, restaurant);
            return restaurant;
        }).collect(Collectors.toList());
        return mappingRestaurantDTO(list, latLng);
    }

    public Map<Long, RestaurantDTO> mappingRestaurantDTO(List<Restaurant> restaurantsList, LatLngDTO latLng) {

        if (CollectionUtils.isEmpty(restaurantsList)) {
            return Collections.emptyMap();
        }

        // 餐厅
        List<Long> restaurantIds = CustomCollectionUtil.listColumn(restaurantsList, Restaurant::getId);

        // 评价
        Map<Long, List<RestaurantReview>> reviewMap = restaurantReviewService.listByRestaurantIds(new HashSet<>(restaurantIds));

        Map<String, Map<Long, BigDecimal>> restaurantAvgGiftCountMap = baseGiftCountHandleService.getRestaurantAvgGiftCountMap(reviewMap);

        // 国家
        List<Long> countryIds = CustomCollectionUtil.listColumn(restaurantsList, Restaurant::getCountryId);
        List<GeoCountry> countryList = geoCountryService.listByIds(countryIds);
        Map<Long, GeoCountry> countryMap = CustomCollectionUtil.mapping(countryList, GeoCountry::getId);

        // 货币
        List<String> currencyCodeList = CustomCollectionUtil.listColumn(countryList, GeoCountry::getCurrencyCode);
        List<Currency> currencyList = currencyService.listByCodes(currencyCodeList);
        Map<String, Currency> currencyMap = CustomCollectionUtil.mapping(currencyList, Currency::getCode);

        List<RestaurantDTO> dtoList = restaurantsList.stream().map(item -> {
            RestaurantDTO dto = appRestaurantConverter.restaurant2DTO(item);
            BigDecimal avgSpent = BigDecimal.ZERO;
            BigDecimal distance = BigDecimal.ZERO;

            // 国家
            GeoCountry country = countryMap.get(item.getCountryId());
            String currencyCode = null;
            if (Objects.nonNull(country)) {
                currencyCode = country.getCurrencyCode();
            }

            // 货币
            Currency currency = null;
            Symbols symbol = null;
            if (Objects.nonNull(currencyCode)) {
                currency = currencyMap.get(currencyCode);
            }
            // 平均消费转换为当地货币
            if (Objects.nonNull(currency)) {
                symbol = Symbols.getBySymbol(currency.getCode());
                BigDecimal price = baseExchangeRatesService.getPriceBySymbol(symbol);
                if (Objects.nonNull(price)) {
                    avgSpent = item.getAvgSpentUsd().multiply(price).setScale(2, RoundingMode.HALF_UP);
                }
            }

            // 餐厅距离
            if (Objects.nonNull(latLng)) {
                boolean containNull = Arrays.asList(item.getLat(), item.getLng(), latLng.getLat(), latLng.getLng()).contains(null);
                if (!containNull) {
                    distance = new BigDecimal(DistanceCalculator.distance(item.getLat().doubleValue(),
                            item.getLng().doubleValue(), latLng.getLat().doubleValue(), latLng.getLng().doubleValue()));
                }
            }

            // 餐厅评价统计
            List<RestaurantReview> restaurantReviews = reviewMap.get(item.getId());
            BaseAvgGiftCountDTO restaurantAvgGiftCount = baseGiftCountHandleService.getRestaurantAvgGiftCount(restaurantReviews, restaurantAvgGiftCountMap);

            dto.setAvgSpent(avgSpent);
            dto.setCurrency(baseCurrencyConverter.toDTO(currency));
            dto.setAvgEarnedUnit(EarningCurrencyUnit.EXP);
            dto.setDistance(distance.setScale(2, RoundingMode.HALF_UP));
            dto.setBaseAvgGiftCount(restaurantAvgGiftCount);
            return dto;
        }).collect(Collectors.toList());

        return CustomCollectionUtil.mapping(dtoList, RestaurantDTO::getId);
    }

    public Map<Long, RestaurantReviewDTO> mappingReviewDTO(List<RestaurantReview> reviewList, CurrentUser currentUser) {
        long currMemberId = Objects.nonNull(currentUser) ? currentUser.getId() : -1L;

        if (CollectionUtils.isEmpty(reviewList)) {
            return Collections.emptyMap();
        }

        List<Long> reviewIds = CustomCollectionUtil.listColumn(reviewList, RestaurantReview::getId);

        // 会员
        List<Long> memberIds = CustomCollectionUtil.listColumn(reviewList, RestaurantReview::getMemberId);
        Map<Long, Member> memberMap = memberService.mappingMember(memberIds);

        // 点赞
        Map<Long, Boolean> reviewLikesMap = restaurantReviewLikesService.mappingMemberLikes(reviewIds, currMemberId);

        // 留言数量
        Map<Long, Long> commentsSumMap = restaurantReviewCommentsService.getCommentsSumMapping(reviewIds);
        // 餐厅
        List<Long> restaurantIds = CustomCollectionUtil.listColumn(reviewList, RestaurantReview::getRestaurantId);
        List<Restaurant> restaurantList = restaurantService.listByIds(restaurantIds);
        Map<Long, RestaurantDTO> restaurantDTOMap = mappingRestaurantDTO(restaurantList, null);

        // 是否允许编辑
        Map<Long, Boolean> allowUpdateMap = restaurantReviewService.mappingAllowUpdate(reviewList, currMemberId);
        Map<Long, Long> reviewChangeCountMap = restaurantReviewChangeService.countByReviewIds(reviewIds);

        Map<Long, BaseGiftCountDTO> baseGiftCountMap = baseGiftCountHandleService.getReviewsBaseGiftCount(new HashSet<>(reviewIds));

        List<RestaurantReviewDTO> dtoList = reviewList.stream().map(item -> {
            RestaurantReviewDTO dto = appRestaurantReviewConverter.intoDTO(item);
            dto.setEarnedUnit(EarningCurrencyUnit.EXP);

            // 餐厅
            RestaurantDTO restaurantDTO = restaurantDTOMap.get(item.getRestaurantId());
            if (Objects.nonNull(restaurantDTO)) {
                dto.setRestaurant(restaurantDTO);
                dto.setRestaurantName(restaurantDTO.getName());
            }

            // 用户名
            Member member = memberMap.get(item.getMemberId());
            if (Objects.nonNull(member)) {
                dto.setUsername(member.getNickname());
            }

            // 点赞状态
            dto.setLikes(reviewLikesMap.get(item.getId()));

            // 留言数量
            Optional.ofNullable(commentsSumMap.get(item.getId())).ifPresent(dto::setCommentsNum);

            // 获取当前评价的赠送记录
            Optional.ofNullable(baseGiftCountMap.get(item.getId())).ifPresent(dto::setBaseGiftCount);

            // 是否允许编辑
            Long changeTimes = reviewChangeCountMap.getOrDefault(item.getId(), 0L);
            dto.setAllowModification(changeTimes == 0 && allowUpdateMap.getOrDefault(item.getId(), false));

            return dto;
        }).collect(Collectors.toList());

        return CustomCollectionUtil.mapping(dtoList, RestaurantReviewDTO::getReviewId);
    }




}
