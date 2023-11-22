package com.digcoin.snapx.server.admin.system.service;

import com.digcoin.snapx.domain.system.entity.SystemSetting;
import com.digcoin.snapx.domain.system.error.SystemSettingError;
import com.digcoin.snapx.domain.system.service.SystemSettingService;
import com.digcoin.snapx.server.base.system.converter.AdminSystemSettingConverter;
import com.digcoin.snapx.server.base.system.dto.AdminSystemSettingDTO;
import com.digcoin.snapx.server.base.system.vo.AdminSystemSettingVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 15:54
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminSystemSettingService {

    private final SystemSettingService systemSettingService;

    private final AdminSystemSettingConverter adminSystemSettingConverter;

    public void editSystemSetting(AdminSystemSettingDTO dto) {
        SystemSetting systemSetting = adminSystemSettingConverter.fromDTO(dto);
        systemSettingService.updateSystemSetting(systemSetting);
    }

    public AdminSystemSettingVO getSystemSetting() {
       return Optional.ofNullable(systemSettingService.findSystemSetting()).map(
               adminSystemSettingConverter::intoVO).orElseThrow(SystemSettingError.SYS_SETTING_NOT_INIT::withDefaults);
    }
}
