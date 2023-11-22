package com.digcoin.snapx.core.web;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpUtil;

import java.util.Optional;

public class CurrentUserProvider {

    public Optional<CurrentUser> getCurrentUser() {
        if (isLogin()) {
            return Optional.ofNullable(StpUtil.getSession().get(CurrentUser.IDENTITY))
                    .map(x -> (CurrentUser) x);
        } else {
            return Optional.empty();
        }
    }

    private boolean isLogin() {
        if (!SaManager.getSaTokenContextOrSecond().isValid()) {
            return false;
        }
        return StpUtil.isLogin();
    }

}
