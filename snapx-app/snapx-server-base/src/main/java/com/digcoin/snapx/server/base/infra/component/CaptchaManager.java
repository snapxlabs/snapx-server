package com.digcoin.snapx.server.base.infra.component;

import com.digcoin.snapx.server.base.infra.dto.CaptchaDTO;

public interface CaptchaManager {

    CaptchaDTO send(CaptchaType type, String receiver);

    boolean verify(CaptchaType type, String receiver, String code);

    void clean(CaptchaType type, String receiver);

}
