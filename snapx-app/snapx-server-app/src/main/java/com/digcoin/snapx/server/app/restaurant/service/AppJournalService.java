package com.digcoin.snapx.server.app.restaurant.service;

import com.digcoin.snapx.core.common.util.CustomCollectionUtil;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.restaurant.bo.JournalPageDailyJournalsQryDTO;
import com.digcoin.snapx.domain.restaurant.bo.JournalPageJournalsQryDTO;
import com.digcoin.snapx.domain.restaurant.entity.Journal;
import com.digcoin.snapx.domain.restaurant.entity.JournalDaily;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import com.digcoin.snapx.domain.restaurant.service.JournalService;
import com.digcoin.snapx.domain.restaurant.service.RestaurantReviewService;
import com.digcoin.snapx.domain.system.constant.EarningCurrencyUnit;
import com.digcoin.snapx.server.app.restaurant.converter.AppJournalConverter;
import com.digcoin.snapx.server.app.restaurant.dto.RestaurantReviewDTO;
import com.digcoin.snapx.server.app.restaurant.vo.JournalPageDailyJournalsVO;
import com.digcoin.snapx.server.app.restaurant.vo.JournalPageJournalsVO;
import com.digcoin.snapx.server.base.member.dto.BaseTotalGiftCountDTO;
import com.digcoin.snapx.server.base.member.service.BaseGiftCountHandleService;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/2 12:26
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppJournalService {

    private final JournalService journalService;
    private final RestaurantReviewService restaurantReviewService;
    private final AppJournalConverter appJournalConverter;
    private final BaseGiftCountHandleService baseGiftCountHandleService;
    private final AppRestaurantAggregationService appRestaurantAggregationService;

    public PageResult<JournalPageDailyJournalsVO> pageDailyJournals(JournalPageDailyJournalsQryDTO dto, CurrentUser currentUser) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<JournalDaily> list = journalService.listDailyJournalByMemberId(currentUser.getId());
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.converter(list, Collections.emptyList());
        }

        List<LocalDate> dates = journalService.listDates(list);
        Map<LocalDate, List<Journal>> dateListMap = journalService.mappingMemberJournals(dates, currentUser.getId());

        // 餐厅评价
        List<Long> reviewIds = journalService.listReviewIds(dateListMap);
        List<RestaurantReview> reviewList = restaurantReviewService.listByIds(reviewIds);
        Map<Long, RestaurantReviewDTO> reviewDTOMap = appRestaurantAggregationService.mappingReviewDTO(reviewList, currentUser);

        List<JournalPageDailyJournalsVO> voList = list.stream().map(item -> {
            JournalPageDailyJournalsVO vo = appJournalConverter.journalDaily2JournalPageDailyJournalsVO(item);
            vo.setTotalEarnedUnit(EarningCurrencyUnit.EXP);

            // 餐厅评价
            List<RestaurantReviewDTO> reviewDTOs = new ArrayList<>();
            List<Journal> journals = dateListMap.getOrDefault(item.getDate(), Collections.emptyList());
            for (Journal journal : journals) {
                RestaurantReviewDTO reviewDTO = reviewDTOMap.get(journal.getReviewId());
                if (Objects.nonNull(reviewDTO)) {
                    reviewDTOs.add(reviewDTO);
                }
            }
            vo.setReviews(reviewDTOs);

            return vo;
        }).collect(Collectors.toList());

        return PageResult.converter(list, voList);
    }

    public PageResult<JournalPageJournalsVO> pageJournals(JournalPageJournalsQryDTO dto, CurrentUser currentUser) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<Journal> list = journalService.listJournalByMemberId(currentUser.getId());
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.converter(list, Collections.emptyList());
        }

        // 餐厅评价
        List<Long> reviewIds = CustomCollectionUtil.listColumn(list, Journal::getReviewId);
        List<RestaurantReview> reviewList = restaurantReviewService.listByIds(reviewIds);
        Map<Long, RestaurantReviewDTO> reviewDTOMap = appRestaurantAggregationService.mappingReviewDTO(reviewList, currentUser);

        List<JournalPageJournalsVO> voList = list.stream().map(item -> {
            JournalPageJournalsVO vo = new JournalPageJournalsVO();
            BeanUtils.copyProperties(item, vo);
            vo.setEarnedUnit(EarningCurrencyUnit.EXP);
            vo.setReview(reviewDTOMap.get(item.getReviewId()));
            return vo;
        }).collect(Collectors.toList());

        return PageResult.converter(list, voList);
    }

    public BaseTotalGiftCountDTO getJournalGiftTotal(Long memberId) {
        return baseGiftCountHandleService.getRestaurantGiftTotal(memberId);
    }
}
