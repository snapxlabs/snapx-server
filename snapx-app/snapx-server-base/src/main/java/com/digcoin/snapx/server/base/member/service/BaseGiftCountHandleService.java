package com.digcoin.snapx.server.base.member.service;

import com.digcoin.snapx.core.common.util.SnapxRandomUtil;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import com.digcoin.snapx.domain.system.entity.SystemSetting;
import com.digcoin.snapx.domain.system.service.SystemSettingService;
import com.digcoin.snapx.domain.trade.bo.SimpleAccountsDetails;
import com.digcoin.snapx.domain.trade.constant.BaseAccountsFinancialSubject;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import com.digcoin.snapx.domain.trade.service.UsdcAccountsService;
import com.digcoin.snapx.server.base.member.dto.BaseAvgGiftCountDTO;
import com.digcoin.snapx.server.base.member.dto.BaseGiftCountDTO;
import com.digcoin.snapx.server.base.member.dto.BaseTotalGiftCountDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/7 15:37
 * @description todo 该类后期改造成工厂
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BaseGiftCountHandleService {

    private final SystemSettingService systemSettingService;

    private final UsdcAccountsService usdcAccountsService;

    private final PointsAccountsService pointsAccountsService;

    private static final String USDC = "USDC";

    private static final String EXP = "EXP";

    @Transactional(rollbackFor = Exception.class)
    public BaseGiftCountDTO increaseActivityGiftCount(Long businessId,
                                                      Long memberId,
                                                      Boolean isSpec) {

        SystemSetting systemSetting = systemSettingService.findSystemSetting();

        BaseGiftCountDTO dto = new BaseGiftCountDTO();

        // 获取普通积分和usdc
        BigDecimal totalPoint = new BigDecimal(0);
        BigDecimal totalUsdc = new BigDecimal(0);
        if (Objects.nonNull(systemSetting.getGiftPointsCount())) {
            BigDecimal bigDecimal = BigDecimal.valueOf(systemSetting.getGiftPointsCount()).setScale(2, RoundingMode.HALF_UP);
            pointsAccountsService.increasePointsForActivity(memberId, businessId, BaseAccountsFinancialSubject.ACTIVITY_BONUS_POINT, bigDecimal);
            totalPoint = totalPoint.add(bigDecimal);
            dto.setGiftPointsCount(bigDecimal);
        }
        if (Objects.nonNull(systemSetting.getGiftUsdcMinCount()) && Objects.nonNull(systemSetting.getGiftUsdcMaxCount())) {
            BigDecimal bigDecimal = SnapxRandomUtil.getRandom(systemSetting.getGiftUsdcMinCount(), systemSetting.getGiftUsdcMaxCount());
            usdcAccountsService.increasePointsForActivity(memberId, businessId, BaseAccountsFinancialSubject.ACTIVITY_BONUS_USDC, bigDecimal);
            totalUsdc = totalUsdc.add(bigDecimal);
            dto.setGiftUsdcCount(bigDecimal);
        }

        // 获取精选积分和usdc
        boolean spec = Objects.isNull(isSpec) ? Boolean.FALSE : isSpec;
        if (spec) {
            if (Objects.nonNull(systemSetting.getSpecGiftPointsCount())) {
                BigDecimal bigDecimal = BigDecimal.valueOf(systemSetting.getSpecGiftPointsCount()).setScale(2, RoundingMode.HALF_UP);
                pointsAccountsService.increasePointsForActivity(memberId, businessId, BaseAccountsFinancialSubject.ACTIVITY_SPEC_BONUS_POINT, bigDecimal);
                totalPoint = totalPoint.add(bigDecimal);
                dto.setSpecGiftPointsCount(bigDecimal);
            }
            if (Objects.nonNull(systemSetting.getSpecGiftUsdcMinCount()) && Objects.nonNull(systemSetting.getSpecGiftUsdcMaxCount())) {
                BigDecimal bigDecimal = SnapxRandomUtil.getRandom(systemSetting.getSpecGiftUsdcMinCount(), systemSetting.getSpecGiftUsdcMaxCount());
                usdcAccountsService.increasePointsForActivity(memberId, businessId, BaseAccountsFinancialSubject.ACTIVITY_SPEC_BONUS_USDC, bigDecimal);
                totalUsdc = totalUsdc.add(bigDecimal);
                dto.setSpecGiftUsdcCount(bigDecimal);
            }
            // 获取额外精选
            if (systemSetting.getIsSpecExtraGift() && Objects.nonNull(systemSetting.getSpecExtraGiftUsdcMinCount()) && Objects.nonNull(systemSetting.getSpecExtraGiftUsdcMaxCount())) {
                BigDecimal bigDecimal = SnapxRandomUtil.getRandom(systemSetting.getSpecExtraGiftUsdcMinCount(), systemSetting.getSpecExtraGiftUsdcMaxCount());
                usdcAccountsService.increasePointsForActivity(memberId, businessId, BaseAccountsFinancialSubject.ACTIVITY_SPEC_EXTRA_BONUS_USDC, bigDecimal);
                totalUsdc = totalUsdc.add(bigDecimal);
                dto.setSpecGiftExtraUsdcCount(bigDecimal);
            }
        }
        dto.setTotalGiftPointsCount(totalPoint.equals(BigDecimal.ZERO) ? null : totalPoint);
        dto.setTotalGiftUsdcCount(totalUsdc.equals(BigDecimal.ZERO) ? null : totalUsdc);
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseGiftCountDTO increaseRestaurantGiftCount(Long businessId,
                                                        Long memberId,
                                                        BigDecimal quantity,
                                                        Boolean isSpec) {

        SystemSetting systemSetting = systemSettingService.findSystemSetting();

        BaseGiftCountDTO dto = new BaseGiftCountDTO();

        // 获取普通积分和usdc
        BigDecimal totalPoint = new BigDecimal(0);
        BigDecimal totalUsdc = new BigDecimal(0);
        if (Objects.nonNull(systemSetting.getGiftPointsCount())) {
            BigDecimal bigDecimal = BigDecimal.valueOf(systemSetting.getGiftPointsCount()).multiply(quantity).setScale(2, RoundingMode.HALF_UP);
            pointsAccountsService.increasePointsForEvaluate(memberId, businessId, BaseAccountsFinancialSubject.RESTAURANT_BONUS_POINT, bigDecimal);
            totalPoint = totalPoint.add(bigDecimal);
            dto.setGiftPointsCount(bigDecimal);
        }
        if (Objects.nonNull(systemSetting.getGiftUsdcMinCount()) && Objects.nonNull(systemSetting.getGiftUsdcMaxCount())) {
            BigDecimal bigDecimal = SnapxRandomUtil.getRandom(systemSetting.getGiftUsdcMinCount(), systemSetting.getGiftUsdcMaxCount());
            usdcAccountsService.increasePointsForEvaluate(memberId, businessId, BaseAccountsFinancialSubject.RESTAURANT_BONUS_USDC, bigDecimal);
            totalUsdc = totalUsdc.add(bigDecimal);
            dto.setGiftUsdcCount(bigDecimal);
        }

        // 获取精选积分和usdc
        boolean spec = Objects.isNull(isSpec) ? Boolean.FALSE : isSpec;
        if (spec) {
            if (Objects.nonNull(systemSetting.getSpecGiftPointsCount())) {
                BigDecimal bigDecimal = BigDecimal.valueOf(systemSetting.getSpecGiftPointsCount()).multiply(quantity).setScale(2, RoundingMode.HALF_UP);
                pointsAccountsService.increasePointsForEvaluate(memberId, businessId, BaseAccountsFinancialSubject.RESTAURANT_SPEC_BONUS_POINT, bigDecimal);
                totalPoint = totalPoint.add(bigDecimal);
                dto.setSpecGiftPointsCount(bigDecimal);
            }
            if (Objects.nonNull(systemSetting.getSpecGiftUsdcMinCount()) && Objects.nonNull(systemSetting.getSpecGiftUsdcMaxCount())) {
                BigDecimal bigDecimal = SnapxRandomUtil.getRandom(systemSetting.getSpecGiftUsdcMinCount(), systemSetting.getSpecGiftUsdcMaxCount());
                usdcAccountsService.increasePointsForEvaluate(memberId, businessId, BaseAccountsFinancialSubject.RESTAURANT_SPEC_BONUS_USDC, bigDecimal);
                totalUsdc = totalUsdc.add(bigDecimal);
                dto.setSpecGiftUsdcCount(bigDecimal);
            }
            // 获取额外精选
            if (systemSetting.getIsSpecExtraGift() && Objects.nonNull(systemSetting.getSpecExtraGiftUsdcMinCount()) && Objects.nonNull(systemSetting.getSpecExtraGiftUsdcMaxCount())) {
                BigDecimal bigDecimal = SnapxRandomUtil.getRandom(systemSetting.getSpecExtraGiftUsdcMinCount(), systemSetting.getSpecExtraGiftUsdcMaxCount());
                usdcAccountsService.increasePointsForEvaluate(memberId, businessId, BaseAccountsFinancialSubject.RESTAURANT_SPEC_EXTRA_BONUS_USDC, bigDecimal);
                totalUsdc = totalUsdc.add(bigDecimal);
                dto.setSpecGiftExtraUsdcCount(bigDecimal);
            }
        }
        dto.setTotalGiftPointsCount(totalPoint.equals(BigDecimal.ZERO) ? null : totalPoint);
        dto.setTotalGiftUsdcCount(totalUsdc.equals(BigDecimal.ZERO) ? null : totalUsdc);
        return dto;
    }

    public BaseGiftCountDTO buildActivityBaseGiftCountDTO(Long businessId,
                                                          Long memberId) {
        List<SimpleAccountsDetails> usdcSimpleAccountsDetails = usdcAccountsService.listAccountsDetailsIncreaseForActivity(memberId, businessId);
        List<SimpleAccountsDetails> pointSimpleAccountsDetails = pointsAccountsService.listAccountsDetailsIncreaseForActivity(memberId, businessId);
        usdcSimpleAccountsDetails.addAll(pointSimpleAccountsDetails);
        return buildBaseGiftCountDTO(usdcSimpleAccountsDetails);
    }

    public Map<Long, BaseGiftCountDTO> getReviewsBaseGiftCount(Set<Long> businessIds) {
        Map<Long, List<SimpleAccountsDetails>> usdcSimpleAccountsDetailsMap = usdcAccountsService.listAccountsDetailsIncreaseForEvaluate(businessIds)
                .stream().collect(Collectors.groupingBy(s -> Long.parseLong(s.getVoucher())));
        Map<Long, List<SimpleAccountsDetails>> pointsSimpleAccountsDetailsMap = pointsAccountsService.listAccountsDetailsIncreaseForEvaluate(businessIds)
                .stream().collect(Collectors.groupingBy(s -> Long.parseLong(s.getVoucher())));
        return businessIds.stream().map(id -> {
            List<SimpleAccountsDetails> totalList = new ArrayList<>();
            Optional.ofNullable(usdcSimpleAccountsDetailsMap.get(id)).ifPresent(totalList::addAll);
            Optional.ofNullable(pointsSimpleAccountsDetailsMap.get(id)).ifPresent(totalList::addAll);
            BaseGiftCountDTO baseGiftCountDTO = buildBaseGiftCountDTO(totalList);
            baseGiftCountDTO.setBusinessId(id);
            return baseGiftCountDTO;
        }).collect(Collectors.toMap(BaseGiftCountDTO::getBusinessId, Function.identity()));
    }

    private BaseGiftCountDTO buildBaseGiftCountDTO(List<SimpleAccountsDetails> list) {
        BaseGiftCountDTO dto = new BaseGiftCountDTO();
        if (CollectionUtils.isEmpty(list)) {
            return new BaseGiftCountDTO();
        }
        BigDecimal totalPoint = new BigDecimal(0);
        BigDecimal totalUsdc = new BigDecimal(0);
        for (SimpleAccountsDetails details : list) {
            switch (details.getSubject()) {
                case BaseAccountsFinancialSubject.ACTIVITY_BONUS_POINT:
                case BaseAccountsFinancialSubject.RESTAURANT_BONUS_POINT:
                    totalPoint = totalPoint.add(details.getAmount());
                    dto.setGiftPointsCount(details.getAmount());
                    break;
                case BaseAccountsFinancialSubject.ACTIVITY_BONUS_USDC:
                case BaseAccountsFinancialSubject.RESTAURANT_BONUS_USDC:
                    totalUsdc = totalUsdc.add(details.getAmount());
                    dto.setGiftUsdcCount(details.getAmount());
                    break;
                case BaseAccountsFinancialSubject.ACTIVITY_SPEC_BONUS_POINT:
                case BaseAccountsFinancialSubject.RESTAURANT_SPEC_BONUS_POINT:
                    totalPoint = totalPoint.add(details.getAmount());
                    dto.setSpecGiftPointsCount(details.getAmount());
                    break;
                case BaseAccountsFinancialSubject.ACTIVITY_SPEC_BONUS_USDC:
                case BaseAccountsFinancialSubject.RESTAURANT_SPEC_BONUS_USDC:
                    totalUsdc = totalUsdc.add(details.getAmount());
                    dto.setSpecGiftUsdcCount(details.getAmount());
                    break;
                case BaseAccountsFinancialSubject.ACTIVITY_SPEC_EXTRA_BONUS_USDC:
                case BaseAccountsFinancialSubject.RESTAURANT_SPEC_EXTRA_BONUS_USDC:
                    totalUsdc = totalUsdc.add(details.getAmount());
                    dto.setSpecGiftExtraUsdcCount(details.getAmount());
                    break;
                default:
                    break;
            }
        }
        dto.setTotalGiftPointsCount(totalPoint.equals(BigDecimal.ZERO) ? null : totalPoint);
        dto.setTotalGiftUsdcCount(totalUsdc.equals(BigDecimal.ZERO) ? null : totalUsdc);
        return dto;
    }

    public BaseTotalGiftCountDTO getRestaurantGiftTotal(Long memberId) {
        BaseTotalGiftCountDTO dto = new BaseTotalGiftCountDTO();
        BigDecimal usdcCount = usdcAccountsService.getEvaluateAmountAggregation(memberId);
        dto.setTotalGiftUsdcCount(usdcCount);
        BigDecimal ponitsCount = pointsAccountsService.getEvaluateAmountAggregation(memberId);
        dto.setTotalGiftPointsCount(ponitsCount);
        return dto;
    }

    public BaseTotalGiftCountDTO getActivityGiftTotal(Long memberId) {
        BaseTotalGiftCountDTO dto = new BaseTotalGiftCountDTO();
        BigDecimal usdcCount = usdcAccountsService.getActivityAmountAggregation(memberId);
        dto.setTotalGiftUsdcCount(usdcCount);
        BigDecimal ponitsCount = pointsAccountsService.getActivityAmountAggregation(memberId);
        dto.setTotalGiftPointsCount(ponitsCount);
        return dto;
    }

    public Map<String, Map<Long, BigDecimal>> getRestaurantAvgGiftCountMap(Map<Long, List<RestaurantReview>> reviewMap) {
        if (reviewMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Set<Long> reviewIds = new HashSet<>();
        for (Map.Entry<Long, List<RestaurantReview>> entry : reviewMap.entrySet()) {
            reviewIds.addAll(entry.getValue().stream().map(RestaurantReview::getId).collect(Collectors.toSet()));
        }
        Map<Long, BigDecimal> pointsMap = pointsAccountsService.getAccountsVoucherAmountMapForEvaluate(reviewIds);
        Map<Long, BigDecimal> usdcMap = usdcAccountsService.getAccountsVoucherAmountMapForEvaluate(reviewIds);
        Map<String, Map<Long, BigDecimal>> result = new HashMap<>(2);
        result.put(USDC, usdcMap);
        result.put(EXP, pointsMap);
        return result;
    }

    public BaseAvgGiftCountDTO getRestaurantAvgGiftCount(List<RestaurantReview> restaurantReviews, Map<String, Map<Long, BigDecimal>> resultsMap) {
        if (CollectionUtils.isEmpty(restaurantReviews)) {
            return null;
        }
        if (resultsMap.isEmpty()) {
            return null;
        }
        List<BigDecimal> pointsResult = new ArrayList<>();
        List<BigDecimal> usdcResult = new ArrayList<>();
        for (RestaurantReview restaurantReview : restaurantReviews) {
            Optional.ofNullable(resultsMap.get(USDC).get(restaurantReview.getId())).ifPresent(usdcResult::add);
            Optional.ofNullable(resultsMap.get(EXP).get(restaurantReview.getId())).ifPresent(pointsResult::add);
        }
        BaseAvgGiftCountDTO dto = new BaseAvgGiftCountDTO();

        double pointsValue = 0D;
        if (CollectionUtils.isNotEmpty(pointsResult)) {
            pointsValue = pointsResult.stream().collect(Collectors.averagingDouble(BigDecimal::doubleValue));
        }
        dto.setAvgGiftPointsCount(BigDecimal.valueOf(pointsValue).setScale(2, RoundingMode.HALF_UP));

        double usdcValue = 0D;
        if (CollectionUtils.isNotEmpty(usdcResult)) {
            usdcValue = usdcResult.stream().collect(Collectors.averagingDouble(BigDecimal::doubleValue));
        }
        dto.setAvgGiftUsdcCount(BigDecimal.valueOf(usdcValue).setScale(2, RoundingMode.HALF_UP));
        return dto;
    }
}
