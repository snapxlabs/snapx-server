package com.digcoin.snapx.server.app.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digcoin.snapx.core.common.util.DistanceCalculator;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.Pageable;
import com.digcoin.snapx.domain.member.bo.MemberActivityQueryBO;
import com.digcoin.snapx.domain.member.entity.MemberActivity;
import com.digcoin.snapx.domain.member.error.MemberActivityError;
import com.digcoin.snapx.domain.member.service.MemberActivityService;
import com.digcoin.snapx.domain.restaurant.bo.LatLngDTO;
import com.digcoin.snapx.domain.system.error.ActivityError;
import com.digcoin.snapx.server.app.member.converter.AppMemberActivityConverter;
import com.digcoin.snapx.server.app.member.dto.AppMemberActivityDTO;
import com.digcoin.snapx.server.app.member.dto.MemberDTO;
import com.digcoin.snapx.server.app.member.vo.AppMemberActivityVO;
import com.digcoin.snapx.server.app.system.service.AppActivityService;
import com.digcoin.snapx.server.app.system.vo.AppActivityVO;
import com.digcoin.snapx.server.base.member.dto.BaseGiftCountDTO;
import com.digcoin.snapx.server.base.member.dto.BaseTotalGiftCountDTO;
import com.digcoin.snapx.server.base.member.service.BaseGiftCountHandleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/3 23:22
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppMemberActivityService {

    private final BaseGiftCountHandleService baseGiftCountHandleService;

    private final MemberAppService memberAppService;

    private final AppActivityService appActivityService;

    private final MemberActivityService memberActivityService;

    private final AppMemberActivityConverter appMemberActivityConverter;


    @Transactional(rollbackFor = Exception.class)
    public AppMemberActivityVO takePartInActivity(AppMemberActivityDTO dto) {
        Long memberId = dto.getMemberId();
        Long activityId = dto.getActivityId();

        AppActivityVO activity = this.getActivity(dto);

        MemberActivity memberActivity = memberActivityService.getMemberActivity(memberId, activityId);
        if (Objects.nonNull(memberActivity)) {
            throw MemberActivityError.MEMBER_ACTIVITY_EXISTS.withDefaults();
        }

        memberActivity = new MemberActivity();
        memberActivity.setActivityId(activityId);
        memberActivity.setMemberId(memberId);
        memberActivity.setLat(dto.getLatLng().getLat().toString());
        memberActivity.setLng(dto.getLatLng().getLng().toString());

        memberActivity = memberActivityService.saveMemberActivity(memberActivity);

        BaseGiftCountDTO baseGiftCountDTO = baseGiftCountHandleService.increaseActivityGiftCount(memberActivity.getId(),
                memberActivity.getMemberId(), activity.getIsSpec());

        AppMemberActivityVO vo = appMemberActivityConverter.intoVO(memberActivity);

        vo.setBaseGiftCount(baseGiftCountDTO);
        vo.setActivity(activity);
        vo.setMember(memberAppService.findMember(memberId));

        return vo;

    }

    public AppMemberActivityVO getMemberActivity(Long memberId, Long activityId) {
        MemberDTO member = memberAppService.findMember(memberId);

        AppActivityVO activity = appActivityService.getActivity(activityId);

        return Optional.ofNullable(memberActivityService.getMemberActivity(memberId, activityId))
                .map(memberActivity -> {
                    AppMemberActivityVO vo = appMemberActivityConverter.intoVO(memberActivity);
                    vo.setBaseGiftCount(baseGiftCountHandleService.buildActivityBaseGiftCountDTO(memberActivity.getId(), memberId));
                    vo.setActivity(activity);
                    vo.setMember(member);
                    return vo;
                }).orElseThrow(MemberActivityError.MEMBER_NOT_EXISTS::withDefaults);
    }


    private AppActivityVO getActivity(AppMemberActivityDTO dto) {

        AppActivityVO activity = appActivityService.getActivity(dto.getActivityId());

        // 判断活动可用
        if (!activity.getIsEnabled()) {
            throw ActivityError.ACTIVITY_ENDED.withDefaults();
        }

        // 判断活动日期
        LocalDate now = LocalDate.now();
        if ((Objects.nonNull(activity.getStartDate()) && activity.getStartDate().isAfter(now)) ||
                (Objects.nonNull(activity.getEndDate()) && activity.getEndDate().isBefore(now))) {
            throw ActivityError.ACTIVITY_NOT_IN_PERIOD.withDefaults();
        }

        // 判断距离
        List<Double> activityLatLng = Arrays.stream(activity.getPosition().split(","))
                .map(Double::parseDouble).collect(Collectors.toList());
        LatLngDTO latLng = dto.getLatLng();
        boolean within = DistanceCalculator.within(activityLatLng.get(0), activityLatLng.get(1),
                latLng.getLat().doubleValue(), latLng.getLng().doubleValue(), 1);
        if (!within) {
            throw ActivityError.ACTIVITY_NOT_IN_DISTANCE.withDefaults();
        }
        return activity;
    }

    public PageResult<AppMemberActivityVO> pageMemberActivity(Long memberId, Pageable pageable) {
        MemberActivityQueryBO queryBO = new MemberActivityQueryBO();
        BeanUtils.copyProperties(pageable, queryBO);
        queryBO.setMemberId(memberId);
        IPage<MemberActivity> iPage = memberActivityService.pageAppMemberActivity(queryBO);
        List<MemberActivity> records = iPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return PageResult.emptyResult();
        }
        MemberDTO member = memberAppService.findMember(memberId);
        Set<Long> activityIds = records.stream().map(MemberActivity::getActivityId).collect(Collectors.toSet());
        Map<Long, AppActivityVO> activityMap = appActivityService.listActivityMapping(activityIds);
        return PageResult.fromPage(iPage, memberActivity -> {
            AppMemberActivityVO vo = appMemberActivityConverter.intoVO(memberActivity);
            Optional.ofNullable(activityMap.get(memberActivity.getActivityId())).ifPresent(vo::setActivity);
            vo.setMember(member);
            vo.setBaseGiftCount(baseGiftCountHandleService.buildActivityBaseGiftCountDTO(memberActivity.getId(), memberActivity.getMemberId()));
            return vo;
        });
    }

    public BaseTotalGiftCountDTO getActivityGiftTotal(Long memberId) {
        return baseGiftCountHandleService.getActivityGiftTotal(memberId);
    }
}
