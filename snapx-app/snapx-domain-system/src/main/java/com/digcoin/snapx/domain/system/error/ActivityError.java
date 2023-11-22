package com.digcoin.snapx.domain.system.error;


import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/1/26 17:31
 * @description
 */
public enum ActivityError implements EnumErrorCodeFactory {

    ACTIVITY_NOT_EXISTS("100001", "Activity does not exist"),

    ACTIVITY_NOT_IN_PERIOD("100002", "Not within the activity period"),

    ACTIVITY_ENDED("100003", "The activity has ended"),

    ACTIVITY_DATE_ERROR("100005","End date before start date"),

    ACTIVITY_POSITION_ERROR("100006","Activity position error"),

    ACTIVITY_NOT_IN_DISTANCE("100004", "Activity not within distance");

    ActivityError(String code, String message) {
        this.update(code, message);
    }
}
