package com.digcoin.snapx.core.common.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

public enum CommonError implements EnumErrorCodeFactory {

    UNEXPECT_ERROR("50000", "The system is busy, please try again later"),
    PARAMETER_ERROR("40000", "Parameter error"),
    PARAMETER_MISS_ERROR("40001", "Missing necessary parameters"),
    DATA_NOT_EXIST("40004", "Data does not exist"),
    REMOTE_SERVICE_ERROR("40005", "Remote service call failed");

    CommonError(String code, String message) {
        this.update(code, message);
    }
}
