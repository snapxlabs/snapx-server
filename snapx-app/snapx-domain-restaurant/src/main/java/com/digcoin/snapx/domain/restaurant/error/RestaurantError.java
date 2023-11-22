package com.digcoin.snapx.domain.restaurant.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/28 16:37
 * @description
 */
public enum RestaurantError implements EnumErrorCodeFactory {

    RESTAURANT_ALREADY_EXISTS("101201", "The restaurant already exists"),

    PLEASE_DELETE_THE_ASSOCIATED_REVIEW_FIRST("101202", "Please delete the associated review first"),

    RESTAURANT_IS_NOT_SPECIFIED("101203", "The restaurant is not specified");

    RestaurantError(String code, String message) {
        this.update(code, message);
    }
}
