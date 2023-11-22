package com.digcoin.snapx.domain.system.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

public enum SystemDomainError implements EnumErrorCodeFactory {

    ADMIN_REGISTER_PASSWORD_ERROR("100000", "两次输入密码不一致"),
    ADMIN_LOGIN_USERNAME_ERROR("100001", "找不到用户名对应的用户"),
    ADMIN_LOGIN_PASSWORD_ERROR("100002", "输入用户名与密码不匹配"),
    ADMIN_LOGIN_DISABLE_ERROR("100003", "用户已停用");

    SystemDomainError(String code, String message) {
        this.update(code, message);
    }
}
