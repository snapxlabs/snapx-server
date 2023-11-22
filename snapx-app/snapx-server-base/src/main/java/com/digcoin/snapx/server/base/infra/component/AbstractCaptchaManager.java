package com.digcoin.snapx.server.base.infra.component;

import com.digcoin.snapx.server.base.infra.dto.CaptchaDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractCaptchaManager implements CaptchaManager {

    protected static final String KEY_PREFIX = "SNAPX:CAPTCHA";
    protected final CaptchaManagerFactory captchaManagerFactory;


    public AbstractCaptchaManager(CaptchaManagerFactory captchaManagerFactory) {
        this.captchaManagerFactory = captchaManagerFactory;
    }

    public String genCode() {
        return RandomStringUtils.randomNumeric(4);
    }

    @Override
    public CaptchaDTO send(CaptchaType type, String receiver) {
        String code = genCode();
        StringRedisTemplate redisTemplate = captchaManagerFactory.getRedisTemplate();
        long timeout = 30L;
        redisTemplate.opsForValue().set(buildKey(type, receiver), code, timeout, TimeUnit.MINUTES);

        CaptchaDTO captcha = new CaptchaDTO();
        captcha.setCaptchaChannel(getCaptchaChannel());
        captcha.setCaptchaType(type);
        captcha.setReceiver(receiver);
        captcha.setExpiredAt(LocalDateTime.now().plusMinutes(timeout));

        log.info("Captcha send type:[{}] receiver:[{}] code:[{}]", type, receiver, code);
        doSend(type, receiver, code);

        return captcha;
    }

    @Override
    public boolean verify(CaptchaType type, String receiver, String code) {
        StringRedisTemplate redisTemplate = captchaManagerFactory.getRedisTemplate();
        String key = buildKey(type, receiver);
        String sentCode = redisTemplate.opsForValue().get(key);
        return Objects.equals(sentCode, code);
    }

    public void clean(CaptchaType type, String receiver) {
        StringRedisTemplate redisTemplate = captchaManagerFactory.getRedisTemplate();
        String key = buildKey(type, receiver);
        redisTemplate.delete(key);
    }

    protected String buildKey(CaptchaType type, String receiver) {
        return String.join(":", KEY_PREFIX, type.toString(), receiver);
    }

    protected abstract CaptchaChannel getCaptchaChannel();

    protected abstract void doSend(CaptchaType type, String receiver, String code);
}
