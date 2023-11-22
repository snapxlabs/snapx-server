package com.digcoin.snapx.domain.infra.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 22:57
 * @description
 */
public enum LocationError implements EnumErrorCodeFactory {

    UNABLE_TO_LOCATE("101000", "Unable to locate"),
    COUNTRIES_ARE_INCONSISTENT("101001", "Countries are inconsistent"),

    ;

    LocationError(String code, String message) {
        this.update(code, message);
    }

}
