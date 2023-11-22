package com.digcoin.snapx.server.app.restaurant.service;

import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.camera.enums.FilmChangeType;
import com.digcoin.snapx.domain.camera.service.FilmMemberService;
import com.digcoin.snapx.domain.infra.bo.LocationBO;
import com.digcoin.snapx.domain.infra.component.exchangerates.Symbols;
import com.digcoin.snapx.domain.infra.service.LocationService;
import com.digcoin.snapx.domain.member.constant.MemberFlag;
import com.digcoin.snapx.domain.member.service.MemberService;
import com.digcoin.snapx.domain.restaurant.bo.ReviewOverviewDTO;
import com.digcoin.snapx.domain.restaurant.entity.Journal;
import com.digcoin.snapx.domain.restaurant.entity.Order;
import com.digcoin.snapx.domain.restaurant.entity.Restaurant;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import com.digcoin.snapx.domain.restaurant.error.OrderError;
import com.digcoin.snapx.domain.restaurant.event.OrderCompletedEvent;
import com.digcoin.snapx.domain.restaurant.service.JournalService;
import com.digcoin.snapx.domain.restaurant.service.OrderService;
import com.digcoin.snapx.domain.restaurant.service.RestaurantReviewService;
import com.digcoin.snapx.domain.restaurant.service.RestaurantService;
import com.digcoin.snapx.domain.system.constant.EarningCurrencyUnit;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import com.digcoin.snapx.server.base.infra.service.BaseExchangeRatesService;
import com.digcoin.snapx.server.app.restaurant.converter.AppOrderServiceConverter;
import com.digcoin.snapx.server.app.restaurant.converter.AppRestaurantConverter;
import com.digcoin.snapx.server.app.restaurant.converter.AppRestaurantReviewConverter;
import com.digcoin.snapx.server.app.restaurant.dto.command.OrderCreateOrderCmd;
import com.digcoin.snapx.server.app.restaurant.vo.OrderCreateOrderVO;
import com.digcoin.snapx.server.base.member.dto.BaseGiftCountDTO;
import com.digcoin.snapx.server.base.member.service.BaseGiftCountHandleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/28 15:42
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppOrderService {

    private final AppOrderServiceConverter appOrderServiceConverter;
    private final AppRestaurantReviewConverter appRestaurantReviewConverter;
    private final OrderService orderService;
    private final RestaurantService restaurantService;
    private final RestaurantReviewService restaurantReviewService;
    private final JournalService journalService;
    private final PointsAccountsService pointsAccountsService;
    private final AppRestaurantConverter appRestaurantConverter;
    private final LocationService locationService;
    private final MemberService memberService;
    private final FilmMemberService filmMemberService;
    private final BaseGiftCountHandleService baseGiftCountHandleService;
    private final BaseExchangeRatesService baseExchangeRatesService;

    @Transactional(rollbackFor = Exception.class)
    public OrderCreateOrderVO createOrder(OrderCreateOrderCmd cmd, CurrentUser currentUser) {
        LocationBO location = locationService.getSystemLocationByLatLng(cmd.getLatLng().getLat(), cmd.getLatLng().getLng());

        // 自动获取餐厅
        Restaurant restaurant = autoGetRestaurant(cmd, location);

        // 创建订单
        Order order = appOrderServiceConverter.fromDTO(cmd);
        order.setCountryId(restaurant.getCountryId());
        order.setLocalityId(restaurant.getLocalityId());
        order.setRestaurantId(restaurant.getId());
        order.setMemberId(currentUser.getId());
        String identity = orderService.getOrderIdentityOrFail(cmd.getLatLng(), cmd.getPhotoUrls(), cmd.getRating(), cmd.getSpend(), cmd.getCurrencyCode(), cmd.getContent());
        order.setIdentity(identity);
        orderService.createOrder(order);

        // 创建评价
        RestaurantReview review = appOrderServiceConverter.dto2review(cmd);
        review.setCountryId(restaurant.getCountryId());
        review.setLocalityId(restaurant.getLocalityId());
        review.setRestaurantId(restaurant.getId());
        review.setMemberId(currentUser.getId());
        review.setOrderId(order.getId());
        review.setEarned(order.getEarned());

        // 计算美元
        Symbols symbol = Symbols.getBySymbol(order.getCurrencyCode());
        if (Objects.nonNull(symbol)) {
            BigDecimal exchangeRates = baseExchangeRatesService.getUsdExchangeRateBySymbol(symbol);
            review.setSpentUsd(order.getSpend().multiply(exchangeRates));
        }
        restaurantReviewService.createReview(review);

        // 更新统计
        ReviewOverviewDTO reviewOverviewDTO = restaurantReviewService.countReviewOverview(restaurant.getId());
        restaurantService.updateReviewOverview(restaurant, reviewOverviewDTO);
        BigDecimal totalEarned = orderService.countTotalEarned(currentUser.getId());
        memberService.updateMemberTotalEarned(currentUser.getId(), totalEarned);

        // 创建日记
        Journal journal = appRestaurantReviewConverter.review2Journal(review);
        journalService.createJournal(journal);

        // 完成订单
        orderService.complete(order);

        // 发放积分
//        pointsAccountsService.increasePointsByEvaluate(order.getMemberId(), review.getId(), order.getEarned());

        long count = Arrays.stream(order.getPhotoUrls().split(",")).count();
        //扣减胶卷
        filmMemberService.saveOrUpdateFilmMember(currentUser.getId(), count * -1L, FilmChangeType.PHOTOGRAPH);

        // 计算积分及usdc
        BaseGiftCountDTO baseGiftCountDTO = baseGiftCountHandleService.increaseRestaurantGiftCount(review.getId(),
                order.getMemberId(), BigDecimal.valueOf(count), restaurant.getSpec());

        memberService.updateSteak(order.getMemberId());
        memberService.updateMemberFlagOff(order.getMemberId(), MemberFlag.NOTIFY_STEAK_LOST);

        return new OrderCreateOrderVO(order.getId(), review.getId(),
                review.getEarned(), EarningCurrencyUnit.EXP, order.getCurrencyCode(), baseGiftCountDTO);
    }

    private Restaurant autoGetRestaurant(OrderCreateOrderCmd cmd, LocationBO location) {
        // 通过 restaurantId 获取，或通过 placeId 创建
        Restaurant restaurant;
        OrderCreateOrderCmd.PlaceDTO placeDTO = cmd.getPlace();
        if (Objects.nonNull(placeDTO) && Objects.nonNull(placeDTO.getId())) {
            // placeId 自动创建餐厅逻辑
            restaurant = restaurantService.findByLPlaceId(placeDTO.getId());
            if (Objects.isNull(restaurant)) {
                restaurant = appRestaurantConverter.fromPlaceDTO(cmd);
                restaurant.setGoogle(true);
                restaurant.setCountryId(location.getCountry().getId());
                restaurant.setLocalityId(location.getLocality().getId());
                restaurant.setVerified(true);
                restaurantService.createRestaurantOrFail(restaurant);
            }
        } else if (Objects.nonNull(cmd.getRestaurantId())) {
            // restaurantId 查餐厅
            restaurant = restaurantService.findByIdOrFail(cmd.getRestaurantId());
        } else {
            throw OrderError.MISSING_RESTAURANTID_OR_PLACEID.withDefaults();
        }
        return restaurant;
    }

    @EventListener
    public void handleOrderCompletedEvent(OrderCompletedEvent event) {
        log.info("receive completed order event", event);
    }

}
