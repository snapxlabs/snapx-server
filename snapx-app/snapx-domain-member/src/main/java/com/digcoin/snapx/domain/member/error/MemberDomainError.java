package com.digcoin.snapx.domain.member.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

public enum MemberDomainError implements EnumErrorCodeFactory {

    EMAIL_HAS_BEEN_BOUND("101000", "Email has been bound by another user"),
    PHONE_HAS_BEEN_BOUND("101001", "Phone has been bound by another user");

    MemberDomainError(String code, String message) {
        this.update(code, message);
    }

}
