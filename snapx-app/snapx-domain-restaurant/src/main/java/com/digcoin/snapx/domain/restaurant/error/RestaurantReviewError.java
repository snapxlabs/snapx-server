package com.digcoin.snapx.domain.restaurant.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/28 00:51
 * @description
 */
public enum RestaurantReviewError implements EnumErrorCodeFactory {

    THE_EARNING_AMOUNT_CANNOT_BE_LESS_THAN_0("102201", "The earning amount cannot be less than 0"),
    YOU_CANT_MODIFY_THE_CURRENT_REVIEW("102202", "You can't modify the current review"),
    NO_CHANGE("102203", "No change"),
    CANNOT_BE_MODIFIED_AGAIN("102204", "Cannot be modified again"),

    ;

    RestaurantReviewError(String code, String message) {
        this.update(code, message);
    }

}
