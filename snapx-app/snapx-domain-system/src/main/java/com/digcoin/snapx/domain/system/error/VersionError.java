package com.digcoin.snapx.domain.system.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/14 0:09
 * @description
 */
public enum VersionError implements EnumErrorCodeFactory {

    VERSION_NUMBER_ERROR("100001", "Incorrect version number format"),

    VERSION_NUMBER_EXISTS("100002","Version number already exists"),

    VERSION_NUMBER_NOT_LATEST("100003", "The version number is not the latest version number"),

    VERSION_NOT_EXISTS("100001", "Version does not exist");

    VersionError(String code, String message) {
        this.update(code, message);
    }
}
