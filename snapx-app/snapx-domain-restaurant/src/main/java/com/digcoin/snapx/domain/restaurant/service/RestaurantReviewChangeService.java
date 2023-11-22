package com.digcoin.snapx.domain.restaurant.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.domain.restaurant.bo.ReviewChangeCountBO;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewChange;
import com.digcoin.snapx.domain.restaurant.enums.ReviewChangeFromType;
import com.digcoin.snapx.domain.restaurant.error.RestaurantReviewError;
import com.digcoin.snapx.domain.restaurant.mapper.RestaurantReviewChangeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/10 22:33
 * @description
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantReviewChangeService {

    private final RestaurantReviewChangeMapper changeMapper;

    public void tryRecordChangeForRateAndContent(RestaurantReview review, Integer rate, String content, ReviewChangeFromType fromType, Long fromId) {
        // 检查是否修改过
        if (existsChangeForRateAndContent(review, fromType, fromId)) {
            throw RestaurantReviewError.CANNOT_BE_MODIFIED_AGAIN.withDefaults();
        }

        RestaurantReview afterReview = new RestaurantReview();
        BeanUtils.copyProperties(review, afterReview);

        afterReview.setRate(rate);
        afterReview.setContent(content);

        record(review, afterReview, fromType, fromId);
    }

    public void tryRecordChangeForEarned(RestaurantReview review, BigDecimal earned, ReviewChangeFromType fromType, Long fromId) {
        RestaurantReview afterReview = new RestaurantReview();
        BeanUtils.copyProperties(review, afterReview);

        afterReview.setEarned(earned);

        record(review, afterReview, fromType, fromId);
    }

    public boolean existsChangeForRateAndContent(RestaurantReview review, ReviewChangeFromType fromType, Long fromId) {
        return changeMapper.selectCount(Wrappers.<RestaurantReviewChange>lambdaQuery()
                .eq(RestaurantReviewChange::getReviewId, review.getId())
                .eq(RestaurantReviewChange::getFromType, fromType)
                .and(fromQueryWrapper -> {
                    fromQueryWrapper.eq(RestaurantReviewChange::getFromMemberId, fromId);
                    fromQueryWrapper.or();
                    fromQueryWrapper.eq(RestaurantReviewChange::getFromAdminId, fromId);
                })) > 0L;
    }

    public void record(RestaurantReview before, RestaurantReview after, ReviewChangeFromType fromType, Long fromId) {
        boolean changed = false;

        RestaurantReviewChange changeRecord = new RestaurantReviewChange();
        changeRecord.setReviewId(before.getId());

        // 修改人信息
        changeRecord.setFromType(fromType);
        if (ReviewChangeFromType.MEMBER.equals(fromType)) {
            changeRecord.setFromMemberId(fromId);
        }
        if (ReviewChangeFromType.ADMIN.equals(fromType)) {
            changeRecord.setFromMemberId(fromId);
        }

        // 变更内容比较
        // 评分
        if (!Objects.equals(before.getRate(), after.getRate())) {
            changeRecord.setBeforeRate(before.getRate());
            changeRecord.setAfterRate(after.getRate());
            changed = true;
        }
        // 内容
        if (!Objects.equals(before.getContent(), after.getContent())) {
            changeRecord.setBeforeContent(before.getContent());
            changeRecord.setAfterContent(after.getContent());
            changed = true;
        }
        // 赚取金额
        if (!Objects.equals(before.getEarned(), after.getEarned())) {
            changeRecord.setBeforeEarned(before.getEarned());
            changeRecord.setAfterEarned(after.getEarned());
            changed = true;
        }

        log.info("changed: {}, before: {}, after: {}", changed, before, after);
        if (!changed) {
            throw RestaurantReviewError.NO_CHANGE.withDefaults();
        }

        changeMapper.insert(changeRecord);
    }

    public Map<Long, Long> countByReviewIds(List<Long> reviewIds) {
        if (CollectionUtils.isEmpty(reviewIds)) {
            return Collections.emptyMap();
        }
        HashMap<Long, ReviewChangeCountBO> countMap = changeMapper.countByReviewIds(reviewIds);
        Map<Long, Long> map = countMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getCount()));
        return map;
    }

}
