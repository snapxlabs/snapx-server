package com.digcoin.snapx.domain.restaurant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.domain.restaurant.entity.Journal;
import com.digcoin.snapx.domain.restaurant.entity.JournalDaily;
import com.digcoin.snapx.domain.restaurant.mapper.JournalDailyMapper;
import com.digcoin.snapx.domain.restaurant.mapper.JournalMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class JournalService {

    private final JournalMapper journalMapper;
    private final JournalDailyMapper journalDailyMapper;

    public void createJournal(Journal journal) {
        journalMapper.insert(journal);

        // 创建每日记录
        if (!existsJournalDailyByDateAndMemberId(journal.getDate(), journal.getMemberId())) {
            JournalDaily journalDaily = new JournalDaily();
            BeanUtils.copyProperties(journal, journalDaily, JournalDaily.ID);
            journalDailyMapper.insert(journalDaily);
        }

        // 更新统计
        updateTotalNumber(journal.getDate(), journal.getMemberId());
        updateTotalEarned(journal.getDate(), journal.getMemberId());
    }

    public Long countTotalNumber(LocalDate todayDate, Long memberId) {
        return journalMapper.selectCount(Wrappers.<Journal>lambdaQuery()
                .eq(Journal::getDate, todayDate)
                .eq(Journal::getMemberId, memberId)
        );
    }

    public BigDecimal countTotalEarned(LocalDate date, Long memberId) {
        QueryWrapper<Journal> wrapper = new QueryWrapper<>();
        wrapper.eq(Journal.DATE, date);
        wrapper.eq(Journal.MEMBER_ID, memberId);
        wrapper.select(String.format("sum(%s)", Journal.EARNED));
        return (BigDecimal) journalMapper.selectObjs(wrapper).get(0);
    }

    public Long updateTotalNumber(LocalDate date, Long memberId) {
        Long count = countTotalNumber(date, memberId);

        LambdaUpdateWrapper<JournalDaily> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(JournalDaily::getDate, date);
        updateWrapper.eq(JournalDaily::getMemberId, memberId);
        updateWrapper.set(JournalDaily::getTotalNumber, count);
        journalDailyMapper.update(null, updateWrapper);
        return count;
    }

    public BigDecimal updateTotalEarned(LocalDate date, Long memberId) {
        BigDecimal sum = countTotalEarned(date, memberId);

        LambdaUpdateWrapper<JournalDaily> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(JournalDaily::getDate, date);
        updateWrapper.eq(JournalDaily::getMemberId, memberId);
        updateWrapper.set(JournalDaily::getTotalEarned, sum);
        journalDailyMapper.update(null, updateWrapper);
        return sum;
    }

    public BigDecimal updateEarnedByReviewId(Long reviewId, BigDecimal earned) {
        Journal journal = findJournalByReviewId(reviewId);
        journal.setEarned(earned);
        journalMapper.updateById(journal);

        BigDecimal sum = countTotalEarned(journal.getDate(), journal.getMemberId());

        LambdaUpdateWrapper<JournalDaily> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(JournalDaily::getDate, journal.getDate());
        updateWrapper.eq(JournalDaily::getMemberId, journal.getMemberId());
        updateWrapper.set(JournalDaily::getTotalEarned, sum);
        journalDailyMapper.update(null, updateWrapper);
        return sum;
    }

    public boolean existsJournalDailyByDateAndMemberId(LocalDate date, Long memberId) {
        return journalDailyMapper.selectCount(Wrappers.<JournalDaily>lambdaQuery()
                .eq(JournalDaily::getDate, date)
                .eq(JournalDaily::getMemberId, memberId)
        ) > 0;
    }

    public JournalDaily findJournalDailyByDateAndMemberId(LocalDate date, Long memberId) {
        return journalDailyMapper.selectOne(Wrappers.<JournalDaily>lambdaQuery()
                .eq(JournalDaily::getDate, date)
                .eq(JournalDaily::getMemberId, memberId)
        );
    }

    public Journal findJournalByReviewId(Long reviewId) {
        return journalMapper.selectOne(Wrappers.<Journal>lambdaQuery()
                .eq(Journal::getReviewId, reviewId)
        );
    }

    public List<JournalDaily> listDailyJournalByMemberId(Long memberId) {
        return journalDailyMapper.selectList(Wrappers.<JournalDaily>lambdaQuery()
                .eq(JournalDaily::getMemberId, memberId)
                .orderByDesc(JournalDaily::getCreateTime)
        );
    }

    public List<Journal> listJournalByMemberId(Long memberId) {
        return journalMapper.selectList(Wrappers.<Journal>lambdaQuery()
                .eq(Journal::getMemberId, memberId)
                .orderByDesc(Journal::getCreateTime)
        );
    }

    public List<Journal> listJournalByDateAndMemberId(LocalDate date, Long memberId) {
        return journalMapper.selectList(Wrappers.<Journal>lambdaQuery()
                .eq(Journal::getDate, date)
                .eq(Journal::getMemberId, memberId)
        );
    }

    public Map<LocalDate, List<Journal>> mappingMemberJournals(List<LocalDate> dates, Long memberId) {
        if (CollectionUtils.isEmpty(dates) || Objects.isNull(memberId)) {
            return Collections.emptyMap();
        }

        Map<LocalDate, List<Journal>> map = new LinkedHashMap<>();
        List<Journal> list = journalMapper.selectList(Wrappers.<Journal>lambdaQuery()
                .in(Journal::getDate, dates)
                .eq(Journal::getMemberId, memberId)
        );

        for (Journal journal : list) {
            LocalDate date = journal.getDate();
            List<Journal> journalList = map.getOrDefault(date, new ArrayList<>());
            journalList.add(journal);
            map.put(date, journalList);
        }

        return map;
    }

    public List<LocalDate> listDates(List<JournalDaily> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(JournalDaily::getDate).collect(Collectors.toList());
    }

    public List<Long> listReviewIds(Map<LocalDate, List<Journal>> dateListMap) {
        if (CollectionUtils.isEmpty(dateListMap)) {
            return Collections.emptyList();
        }

        List<Long> reviewIds = new ArrayList<>();
        dateListMap.forEach((localDate, journals) -> {
            reviewIds.addAll(listReviewIds(journals));
        });
        return reviewIds;
    }

    public List<Long> listRestaurantIds(Map<LocalDate, List<Journal>> dateListMap) {
        if (CollectionUtils.isEmpty(dateListMap)) {
            return Collections.emptyList();
        }

        List<Long> reviewIds = new ArrayList<>();
        dateListMap.forEach((localDate, journals) -> {
            reviewIds.addAll(listRestaurantIds(journals));
        });
        return reviewIds;
    }

    public List<Long> listReviewIds(List<Journal> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(Journal::getReviewId).collect(Collectors.toList());
    }

    public List<Long> listRestaurantIds(List<Journal> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(Journal::getRestaurantId).collect(Collectors.toList());
    }

    public boolean deleteByReviewId(Long id) {
        return journalMapper.delete(Wrappers.<Journal>lambdaQuery().eq(Journal::getReviewId, id)) > 0;
    }
}
