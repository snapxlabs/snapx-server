package com.digcoin.snapx.domain.restaurant.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/2 10:21
 * @description
 */
public enum OrderError implements EnumErrorCodeFactory {

    ORDER_EXISTS("101101", "The order already exists"),

    MISSING_RESTAURANTID_OR_PLACEID("101102", "Missing restaurantId or placeId"),

    ;

    OrderError(String code, String message) {
        this.update(code, message);
    }

}
