package com.digcoin.snapx.domain.restaurant.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/24 23:37
 * @description
 */
public enum RestaurantReviewCommentsError implements EnumErrorCodeFactory {

    REVIEW_COMMENTS_NOT_EXISTS("100001", "Review comments not exists"),

    REVIEW_COMMENTS_CANNOT_DELETE("100001","Your cannot delete this comment");

    RestaurantReviewCommentsError(String code, String message) {
        this.update(code, message);
    }
}
