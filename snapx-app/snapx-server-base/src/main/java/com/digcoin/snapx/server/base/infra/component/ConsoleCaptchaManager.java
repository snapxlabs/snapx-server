package com.digcoin.snapx.server.base.infra.component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleCaptchaManager extends AbstractCaptchaManager implements CaptchaManager {

    public ConsoleCaptchaManager(CaptchaManagerFactory captchaManagerFactory) {
        super(captchaManagerFactory);
    }

    @Override
    protected CaptchaChannel getCaptchaChannel() {
        return CaptchaChannel.CONSOLE;
    }

    @Override
    protected void doSend(CaptchaType type, String receiver, String code) {
        log.info("Captcha sent type:{} code:{}", buildKey(type, receiver), code);
    }

}
