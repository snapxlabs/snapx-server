package com.digcoin.snapx.server.admin.restaurant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.common.util.CustomCollectionUtil;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.camera.enums.FilmChangeType;
import com.digcoin.snapx.domain.camera.service.FilmMemberService;
import com.digcoin.snapx.domain.infra.component.exchangerates.Symbols;
import com.digcoin.snapx.domain.infra.entity.GeoCountry;
import com.digcoin.snapx.domain.infra.entity.GeoLocality;
import com.digcoin.snapx.domain.infra.service.GeoCountryService;
import com.digcoin.snapx.domain.infra.service.GeoLocalityService;
import com.digcoin.snapx.domain.member.constant.MemberFlag;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.service.MemberService;
import com.digcoin.snapx.domain.restaurant.bo.LatLngDTO;
import com.digcoin.snapx.domain.restaurant.bo.ReviewOverviewDTO;
import com.digcoin.snapx.domain.restaurant.entity.Journal;
import com.digcoin.snapx.domain.restaurant.entity.Order;
import com.digcoin.snapx.domain.restaurant.entity.Restaurant;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import com.digcoin.snapx.domain.restaurant.enums.ReviewChangeFromType;
import com.digcoin.snapx.domain.restaurant.enums.Source;
import com.digcoin.snapx.domain.restaurant.error.RestaurantReviewError;
import com.digcoin.snapx.domain.restaurant.mapper.RestaurantReviewMapper;
import com.digcoin.snapx.domain.restaurant.service.*;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import com.digcoin.snapx.server.admin.member.converter.MemberConverter;
import com.digcoin.snapx.server.admin.restaurant.assembler.BaseRestaurantAssembler;
import com.digcoin.snapx.server.admin.restaurant.assembler.BaseRestaurantReviewAssembler;
import com.digcoin.snapx.server.admin.restaurant.dto.RestaurantReviewDTO;
import com.digcoin.snapx.server.admin.restaurant.dto.command.RestaurantReviewCreateReviewCmd;
import com.digcoin.snapx.server.admin.restaurant.dto.command.RestaurantReviewUpdateReviewCmd;
import com.digcoin.snapx.server.admin.restaurant.dto.command.ReviewDeleteReviewCmd;
import com.digcoin.snapx.server.admin.restaurant.dto.command.ReviewUpdateEarnedCmd;
import com.digcoin.snapx.server.admin.restaurant.dto.command.query.RestaurantFindReviewQry;
import com.digcoin.snapx.server.admin.restaurant.dto.command.query.ReviewPageReviewQry;
import com.digcoin.snapx.server.base.infra.assembler.BaseCountryAssembler;
import com.digcoin.snapx.server.base.infra.assembler.BaseLocalityAssembler;
import com.digcoin.snapx.server.base.infra.service.BaseExchangeRatesService;
import com.digcoin.snapx.server.base.member.dto.BaseGiftCountDTO;
import com.digcoin.snapx.server.base.member.service.BaseGiftCountHandleService;
import com.digcoin.snapx.server.base.restaurant.converter.BaseRestaurantReviewConverter;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/27 18:36
 * @description
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AdminRestaurantReviewService {

    private final RestaurantService restaurantService;
    private final BaseRestaurantAssembler baseRestaurantAssembler;

    private final RestaurantReviewService restaurantReviewService;
    private final RestaurantReviewMapper restaurantReviewMapper;
    private final BaseRestaurantReviewAssembler baseRestaurantReviewAssembler;
    private final RestaurantReviewChangeService restaurantReviewChangeService;

    private final GeoCountryService geoCountryService;
    private final BaseCountryAssembler baseCountryAssembler;

    private final GeoLocalityService geoLocalityService;
    private final BaseLocalityAssembler baseLocalityAssembler;

    private final MemberService memberService;
    private final MemberConverter memberConverter;

    private final OrderService orderService;

    private final JournalService journalService;

    private final PointsAccountsService pointsAccountsService;
    private final FilmMemberService filmMemberService;

    private final BaseRestaurantReviewConverter baseRestaurantReviewConverter;
    private final BaseGiftCountHandleService baseGiftCountHandleService;

    private final BaseExchangeRatesService baseExchangeRatesService;

    public PageResult<RestaurantReviewDTO> pageReviews(ReviewPageReviewQry qry) {
        LambdaQueryWrapper<RestaurantReview> queryWrapper = Wrappers.<RestaurantReview>lambdaQuery()
                .orderByDesc(RestaurantReview::getCreateTime);

        if (Objects.nonNull(qry.getId())) {
            queryWrapper.like(RestaurantReview::getId, qry.getId());
        }
        if (Objects.nonNull(qry.getCountryId())) {
            queryWrapper.eq(RestaurantReview::getCountryId, qry.getCountryId());
        }
        if (Objects.nonNull(qry.getLocalityId())) {
            queryWrapper.eq(RestaurantReview::getLocalityId, qry.getLocalityId());
        }
        if (Objects.nonNull(qry.getRestaurantName())) {
            List<Restaurant> restaurants = restaurantService.listByKeyword(qry.getRestaurantName());
            List<Long> restaurantIds = CustomCollectionUtil.listColumn(restaurants, Restaurant::getId);
            if (restaurantIds.isEmpty()) {
                restaurantIds = Arrays.asList(-1L);
            }
            queryWrapper.in(RestaurantReview::getRestaurantId, restaurantIds);
        }
        if (StringUtils.isNotBlank(qry.getContent())) {
            queryWrapper.like(RestaurantReview::getContent, qry.getContent());
        }
        if (Objects.nonNull(qry.getRate()) && qry.getRate().size() == 2) {
            queryWrapper.between(RestaurantReview::getRate, qry.getRate().get(0), qry.getRate().get(1));
        }

        PageHelper.startPage(qry.getPage(), qry.getPageSize());
        List<RestaurantReview> list = restaurantReviewMapper.selectList(queryWrapper);
        if (list.isEmpty()) {
            return PageResult.emptyResult();
        }

        List<Long> reviewIds = CustomCollectionUtil.listColumn(list, RestaurantReview::getId);
        List<RestaurantReviewDTO> dtoList = listByReviewIds(reviewIds);
        return PageResult.converter(list, dtoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteReview(ReviewDeleteReviewCmd cmd) {
        Long id = cmd.getId();

        RestaurantReview review = restaurantReviewService.findByIdOrFail(id);
        Restaurant restaurant = restaurantService.findByIdOrFail(review.getRestaurantId());
        Long memberId = review.getMemberId();

        // 删除评价
        restaurantReviewService.delete(id);

        // 数据来自 APP，那么处理关联，否则默认当作后台创建
        if (Source.APP.equals(review.getSource())) {
            // 删除订单
            orderService.deleteById(review.getOrderId());
        }

        // 删除日记
        journalService.deleteByReviewId(review.getId());

        // 扣减会员积分
        pointsAccountsService.reducePointsByAdminModify(memberId, review.getId(), review.getEarned());

        // 更新会员
        BigDecimal totalEarned = orderService.countTotalEarned(memberId);
        memberService.updateMemberTotalEarned(memberId, totalEarned);

        // 恢复胶卷
        filmMemberService.saveOrUpdateFilmMember(review.getMemberId(),
                Arrays.stream(review.getPhotoUrls().split(",")).count() * 1L,
                FilmChangeType.ADMIN_MODIFY_RESTAURANT_EVALUATE);

        // 更新餐厅统计
        ReviewOverviewDTO reviewOverviewDTO = restaurantReviewService.countReviewOverview(restaurant.getId());
        restaurantService.updateReviewOverview(restaurant, reviewOverviewDTO);

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateEarned(ReviewUpdateEarnedCmd cmd, CurrentUser currentUser) {
        Long id = cmd.getId();
        BigDecimal earned = cmd.getEarned();

        if (earned.compareTo(BigDecimal.ZERO) < 0) {
            throw RestaurantReviewError.THE_EARNING_AMOUNT_CANNOT_BE_LESS_THAN_0.withDefaults();
        }

        RestaurantReview review = restaurantReviewService.findByIdOrFail(id);
        Restaurant restaurant = restaurantService.findByIdOrFail(review.getRestaurantId());
        Long memberId = review.getMemberId();
        BigDecimal before = review.getEarned();

        // 评价
        restaurantReviewChangeService.tryRecordChangeForEarned(review, earned, ReviewChangeFromType.ADMIN, currentUser.getId());
        restaurantReviewService.updateEarned(review, earned);

        // 数据来自 APP，那么处理关联，否则默认当作后台创建
        if (Source.APP.equals(review.getSource())) {
            // 订单
            orderService.updateEarned(review.getOrderId(), earned);
        }

        // 日记
        journalService.updateEarnedByReviewId(review.getId(), earned);

        // 会员积分
        BigDecimal diff = earned.subtract(before);
        if (diff.compareTo(BigDecimal.ZERO) >= 0) {
            pointsAccountsService.increasePointsByAdminModify(memberId, review.getId(), diff.abs());
        } else {
            pointsAccountsService.reducePointsByAdminModify(memberId, review.getId(), diff.abs());
        }

        // 会员
        BigDecimal totalEarned = orderService.countTotalEarned(memberId);
        memberService.updateMemberTotalEarned(memberId, totalEarned);

        // 餐厅
        ReviewOverviewDTO reviewOverviewDTO = restaurantReviewService.countReviewOverview(restaurant.getId());
        restaurantService.updateReviewOverview(restaurant, reviewOverviewDTO);

        return true;
    }

    public RestaurantReviewDTO findReview(RestaurantFindReviewQry qry) {
        List<RestaurantReviewDTO> dtoList = listByReviewIds(Arrays.asList(qry.getId()));
        if (CollectionUtils.isEmpty(dtoList)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("reviewId not found");
        }
        return dtoList.get(0);
    }

    public List<RestaurantReviewDTO> listByReviewIds(List<Long> reviewIds) {
        if (CollectionUtils.isEmpty(reviewIds)) {
            return Collections.emptyList();
        }

        List<RestaurantReview> list = restaurantReviewMapper.selectBatchIds(reviewIds);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        // 国家
        List<Long> countryIds = CustomCollectionUtil.listColumn(list, RestaurantReview::getCountryId);
        List<GeoCountry> countryList = geoCountryService.listByIds(countryIds);
        Map<Long, GeoCountry> countryMap = CustomCollectionUtil.mapping(countryList, GeoCountry::getId);

        // 城市
        List<Long> localityIds = CustomCollectionUtil.listColumn(list, RestaurantReview::getLocalityId);
        List<GeoLocality> localityList = geoLocalityService.listByIds(localityIds);
        Map<Long, GeoLocality> localityMap = CustomCollectionUtil.mapping(localityList, GeoLocality::getId);

        // 餐厅
        List<Long> restaurantIds = CustomCollectionUtil.listColumn(list, RestaurantReview::getRestaurantId);
        List<Restaurant> restaurantList = restaurantService.listByIds(restaurantIds);
        Map<Long, Restaurant> restaurantMap = CustomCollectionUtil.mapping(restaurantList, Restaurant::getId);

        // 会员
        List<Long> memberIds = CustomCollectionUtil.listColumn(list, RestaurantReview::getMemberId);
        List<Member> memberList = memberService.listMember(memberIds);
        Map<Long, Member> memberMap = CustomCollectionUtil.mapping(memberList, Member::getId);

        List<RestaurantReviewDTO> voList = list.stream().map(item -> {
            RestaurantReviewDTO vo = baseRestaurantReviewAssembler.toDTO(item);

            // 国家
            GeoCountry country = countryMap.get(item.getCountryId());
            if (Objects.nonNull(country)) {
                vo.setCountry(baseCountryAssembler.toDTO(country));
            }

            // 城市
            GeoLocality locality = localityMap.get(item.getLocalityId());
            if (Objects.nonNull(locality)) {
                vo.setLocality(baseLocalityAssembler.toDTO(locality));
            }

            // 餐厅
            Restaurant restaurant = restaurantMap.get(item.getRestaurantId());
            if (Objects.nonNull(restaurant)) {
                vo.setRestaurant(baseRestaurantAssembler.toDTO(restaurant));
            }

            // 会员
            Member member = memberMap.get(item.getMemberId());
            if (Objects.nonNull(member)) {
                vo.setMember(memberConverter.intoDTO(member));
            }

            return vo;
        }).collect(Collectors.toList());

        return voList;
    }

    @Transactional(rollbackFor = Exception.class)
    public RestaurantReviewDTO updateReview(RestaurantReviewUpdateReviewCmd cmd) {
        RestaurantReview review = restaurantReviewService.findByIdOrFail(cmd.getId());
        review.setCountryId(cmd.getCountryId());
        review.setLocalityId(cmd.getLocalityId());
        review.setRate(cmd.getRate());
        review.setContent(cmd.getContent());
        if (CollectionUtils.isNotEmpty(cmd.getPhotoUrls())) {
            review.setPhotoUrls(Joiner.on(",").join(cmd.getPhotoUrls()));
        }
        restaurantReviewMapper.updateById(review);
        List<RestaurantReviewDTO> dtoList = listByReviewIds(Arrays.asList(cmd.getId()));
        return dtoList.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    public RestaurantReviewDTO createReview(RestaurantReviewCreateReviewCmd cmd) {
        Restaurant restaurant = restaurantService.findByIdOrFail(cmd.getRestaurantId());
        Member member = memberService.findMemberOrFail(cmd.getMemberId());

        // 创建订单
        Order order = new Order();
        BeanUtils.copyProperties(cmd, order);
        order.setSpend(cmd.getSpent());
        order.setCountryId(restaurant.getCountryId());
        order.setLocalityId(restaurant.getLocalityId());
        order.setRestaurantId(restaurant.getId());
        order.setMemberId(member.getId());
        order.setPhotoUrls(Joiner.on(",").join(cmd.getPhotoUrls()));
        String identity = orderService.getOrderIdentityOrFail(new LatLngDTO(), order.getPhotoUrls(), cmd.getRate(), cmd.getSpent(), cmd.getCurrencyCode(), cmd.getContent());
        order.setIdentity(identity);
        orderService.createOrder(order);

        // 创建评价
        RestaurantReview review = baseRestaurantReviewAssembler.toDataObject(cmd);
        review.setSource(Source.ADMIN);
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
        BigDecimal totalEarned = orderService.countTotalEarned(member.getId());
        memberService.updateMemberTotalEarned(member.getId(), totalEarned);

        // 创建日记
        Journal journal = baseRestaurantReviewConverter.review2Journal(review);
        journalService.createJournal(journal);

        // 完成订单
        orderService.complete(order);

        long count = Arrays.stream(order.getPhotoUrls().split(",")).count();
        //扣减胶卷
        filmMemberService.saveOrUpdateFilmMember(order.getMemberId(), count * -1L, FilmChangeType.PHOTOGRAPH);

        // 计算积分及usdc
        BaseGiftCountDTO baseGiftCountDTO = baseGiftCountHandleService.increaseRestaurantGiftCount(review.getId(),
                order.getMemberId(), BigDecimal.valueOf(count), restaurant.getSpec());

        memberService.updateSteak(order.getMemberId());
        memberService.updateMemberFlagOff(order.getMemberId(), MemberFlag.NOTIFY_STEAK_LOST);

        List<RestaurantReviewDTO> dtoList = listByReviewIds(Arrays.asList(review.getId()));
        return dtoList.get(0);
    }

}
