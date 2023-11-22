package com.digcoin.snapx.domain.system.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.redis.RedisConstant;
import com.digcoin.snapx.domain.system.entity.SystemSetting;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 9:35
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemSettingService {

    private final StringRedisTemplate stringRedisTemplate;

    public SystemSetting findSystemSetting() {
        try {
            String value = stringRedisTemplate.opsForValue().get(RedisConstant.SYSTEM_SETTING);
            if (StringUtils.isBlank(value)) {
                return initSystemSetting();
            }
            return RedisConstant.OBJECT_MAPPER.readValue(value, SystemSetting.class);
        } catch (JsonProcessingException e) {
            throw CommonError.UNEXPECT_ERROR.withDefaults();
        }
    }

    public void updateSystemSetting(SystemSetting systemSetting) {
        try {
            stringRedisTemplate.opsForValue().set(RedisConstant.SYSTEM_SETTING, RedisConstant.OBJECT_MAPPER.writeValueAsString(systemSetting));
        } catch (JsonProcessingException e) {
            throw CommonError.UNEXPECT_ERROR.withDefaults();
        }
    }

    private SystemSetting initSystemSetting() {
        try {
            SystemSetting systemSetting = new SystemSetting();
            systemSetting.setIsRegisterGift(Boolean.TRUE);
            systemSetting.setRegisterGiftCount(10L);
            systemSetting.setIsFollowGift(Boolean.TRUE);
            systemSetting.setFollowGiftCount(10L);
            systemSetting.setIsInviteGift(Boolean.TRUE);
            systemSetting.setInviteGiftCount(10L);
            systemSetting.setBeInvitedGiftCount(10L);
            systemSetting.setUsdcSharingRatio("10");
            systemSetting.setPointsSharingRatio("1");
            systemSetting.setInviteCodeRequired(Boolean.FALSE);

            systemSetting.setGiftPointsCount(5.00D);
            systemSetting.setGiftUsdcMinCount(1.00D);
            systemSetting.setGiftUsdcMaxCount(1.00D);

            systemSetting.setSpecGiftPointsCount(10.00D);
            systemSetting.setSpecGiftUsdcMinCount(2.00D);
            systemSetting.setSpecGiftUsdcMaxCount(2.00D);

            systemSetting.setIsSpecExtraGift(Boolean.TRUE);
            systemSetting.setSpecExtraGiftUsdcMinCount(1.00D);
            systemSetting.setSpecExtraGiftUsdcMaxCount(1.00D);

            stringRedisTemplate.opsForValue().set(RedisConstant.SYSTEM_SETTING, RedisConstant.OBJECT_MAPPER.writeValueAsString(systemSetting));
            return systemSetting;
        } catch (JsonProcessingException e) {
            log.error("initSystemSetting error", e);
        }
        return null;
    }

}
