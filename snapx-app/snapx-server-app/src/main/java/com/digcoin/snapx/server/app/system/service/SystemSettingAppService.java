package com.digcoin.snapx.server.app.system.service;

import com.digcoin.snapx.domain.system.error.SystemSettingError;
import com.digcoin.snapx.domain.system.service.SystemSettingService;
import com.digcoin.snapx.server.base.system.converter.AdminSystemSettingConverter;
import com.digcoin.snapx.server.base.system.vo.AdminSystemSettingVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemSettingAppService {

    private final SystemSettingService systemSettingService;
    private final AdminSystemSettingConverter adminSystemSettingConverter;

    public AdminSystemSettingVO getSystemSetting() {
        return Optional.ofNullable(systemSettingService.findSystemSetting()).map(
                adminSystemSettingConverter::intoVO).orElseThrow(SystemSettingError.SYS_SETTING_NOT_INIT::withDefaults);
    }

}
