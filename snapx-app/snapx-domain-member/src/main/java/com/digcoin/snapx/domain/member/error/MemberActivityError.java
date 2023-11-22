package com.digcoin.snapx.domain.member.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/4 0:36
 * @description
 */
public enum MemberActivityError implements EnumErrorCodeFactory {

    MEMBER_ACTIVITY_EXISTS("100001", "Member has already take part in the activity"),

    MEMBER_NOT_EXISTS("100001", "Member don't take part in the activity");

    MemberActivityError(String code, String message) {
        this.update(code, message);
    }

}
