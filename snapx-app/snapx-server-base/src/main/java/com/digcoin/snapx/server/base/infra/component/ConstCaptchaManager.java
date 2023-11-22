package com.digcoin.snapx.server.base.infra.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

@Slf4j
public class ConstCaptchaManager extends AbstractCaptchaManager implements CaptchaManager {

    private static final String CAPTCHA_CODE = "0000";

    public ConstCaptchaManager(CaptchaManagerFactory captchaManagerFactory) {
        super(captchaManagerFactory);
    }

    @Override
    protected CaptchaChannel getCaptchaChannel() {
        return CaptchaChannel.CONST;
    }

    @Override
    protected void doSend(CaptchaType type, String receiver, String code) {
        StringRedisTemplate redisTemplate = captchaManagerFactory.getRedisTemplate();
        redisTemplate.opsForValue().set(buildKey(type, receiver), CAPTCHA_CODE);
        log.info("Captcha sent type:{} code:{}", buildKey(type, receiver), CAPTCHA_CODE);
    }

}
