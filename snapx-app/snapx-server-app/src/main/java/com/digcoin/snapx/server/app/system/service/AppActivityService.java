package com.digcoin.snapx.server.app.system.service;

import com.digcoin.snapx.domain.system.error.ActivityError;
import com.digcoin.snapx.domain.system.service.ActivityService;
import com.digcoin.snapx.server.app.system.converter.AppActivityConverter;
import com.digcoin.snapx.server.app.system.vo.AppActivityVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/5 18:13
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppActivityService {

    private final ActivityService activityService;

    private final AppActivityConverter appActivityConverter;


    public AppActivityVO getActivity(Long activityId) {
        return Optional.ofNullable(activityService.findById(activityId))
                .map(appActivityConverter::intoVO).orElseThrow(ActivityError.ACTIVITY_NOT_EXISTS::withDefaults);
    }

    public Map<Long, AppActivityVO> listActivityMapping(Set<Long> activityIds) {
        return activityService.findActivityList(activityIds).stream().
                map(appActivityConverter::intoVO).collect(Collectors.toMap(AppActivityVO::getId, Function.identity()));
    }
}
