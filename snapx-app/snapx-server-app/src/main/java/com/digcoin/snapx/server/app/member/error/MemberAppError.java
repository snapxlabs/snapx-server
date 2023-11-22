package com.digcoin.snapx.server.app.member.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

public enum MemberAppError implements EnumErrorCodeFactory {

    EMAIL_NOT_REGISTERED("201000", "Email not registered"),
    EMAIL_NOT_MATCH("201001", "Email not match current Member"),
    ACCOUNT_OR_PASSWORD_INCORRECT("201002", "Account or password incorrect"),
    MEMBER_NOT_EXIST("201003", "Member not exist"),
    ACCOUNT_FREEZE("201004", "Account is freeze"),
    INVITE_CODE_NOT_EXIST("201005", "Invite code not exist"),
    INVITE_CODE_USED("201006", "Invitation code has been used"),
    INVITE_CODE_REQUIRED("201007", "Invite code required"),

    CAN_NOT_UPDATE_NICKNAME("201008", "You’ll be able to change your username again in {0} days."),
    CAN_NOT_BIND_PHONE("201009", "Current User had bound with another phone");

    MemberAppError(String code, String message) {
        this.update(code, message);
    }

}
