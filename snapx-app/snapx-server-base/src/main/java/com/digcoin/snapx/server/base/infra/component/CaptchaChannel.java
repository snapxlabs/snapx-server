package com.digcoin.snapx.server.base.infra.component;

import java.util.Arrays;

public enum CaptchaChannel {
    EMAIL, SMS, CONSOLE, CONST;

    public static CaptchaChannel of(String name) {
        return Arrays.stream(CaptchaChannel.values())
                .filter(x -> x.name().equalsIgnoreCase(name))
                .findAny()
                .orElse(null);
    }
}
