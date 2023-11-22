package com.digcoin.snapx.server.admin.system.service;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.system.bo.ActivityQueryBO;
import com.digcoin.snapx.domain.system.entity.Activity;
import com.digcoin.snapx.domain.system.error.ActivityError;
import com.digcoin.snapx.domain.system.service.ActivityService;
import com.digcoin.snapx.server.admin.system.converter.AdminActivityConverter;
import com.digcoin.snapx.server.admin.system.dto.AdminActivityDTO;
import com.digcoin.snapx.server.admin.system.dto.AdminActivityQueryDTO;
import com.digcoin.snapx.server.admin.system.vo.AdminActivityVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/17 11:34
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminActivityService {

    private final ActivityService activityService;

    private final AdminActivityConverter adminActivityConverter;

    public void createActivity(AdminActivityDTO dto) {
        if (Objects.nonNull(dto.getEndDate()) && Objects.nonNull(dto.getStartDate()) && dto.getEndDate().isBefore(dto.getStartDate())) {
            throw ActivityError.ACTIVITY_DATE_ERROR.withDefaults();
        }
        if(!dto.getPosition().contains(",")){
            throw ActivityError.ACTIVITY_POSITION_ERROR.withDefaults();
        }
        Activity activity = adminActivityConverter.fromDTO(dto);
        activityService.createActivity(activity);
    }

    public PageResult<AdminActivityVO> pageActivity(AdminActivityQueryDTO dto) {
        ActivityQueryBO activityQueryBO = adminActivityConverter.intoBO(dto);
        PageResult<Activity> activityPageResult = activityService.pageActivity(activityQueryBO);
        return PageResult.fromPageResult(activityPageResult, adminActivityConverter::intoVO);
    }

    public AdminActivityVO getActivity(Long id) {
        return Optional.ofNullable(activityService.findById(id)).map(
                adminActivityConverter::intoVO).orElseThrow(ActivityError.ACTIVITY_NOT_EXISTS::withDefaults);
    }

    public void deleteActivity(Long id) {
        Optional<Activity> optional = Optional.ofNullable(activityService.findById(id));
        if (optional.isEmpty()) {
            throw ActivityError.ACTIVITY_NOT_EXISTS.withDefaults();
        }
        activityService.deleteActivity(optional.get());
    }

    public void editActivity(Long id, AdminActivityDTO dto) {
        if (Objects.nonNull(dto.getEndDate()) && Objects.nonNull(dto.getStartDate()) && dto.getEndDate().isBefore(dto.getStartDate())) {
            throw ActivityError.ACTIVITY_DATE_ERROR.withDefaults();
        }
        if(!dto.getPosition().contains(",")){
            throw ActivityError.ACTIVITY_POSITION_ERROR.withDefaults();
        }
        Optional<Activity> optional = Optional.ofNullable(activityService.findById(id));
        if (optional.isEmpty()) {
            throw ActivityError.ACTIVITY_NOT_EXISTS.withDefaults();
        }
        Activity activity = adminActivityConverter.fromDTO(dto);
        activity.setId(id);
        activityService.updateActivity(activity);
    }

    public String getActivitySvg(Long id) {
        Optional<Activity> optional = Optional.ofNullable(activityService.findById(id));
        if (optional.isEmpty()) {
            throw ActivityError.ACTIVITY_NOT_EXISTS.withDefaults();
        }
        return activityService.getActivitySvg(id);
    }
}
