package com.digcoin.snapx.domain.system.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 16:01
 * @description
 */
public enum SystemSettingError implements EnumErrorCodeFactory {

    SYS_SETTING_NOT_INIT("100000", "System setting Not initialized");

    SystemSettingError(String code, String message) {
        this.update(code, message);
    }
}
