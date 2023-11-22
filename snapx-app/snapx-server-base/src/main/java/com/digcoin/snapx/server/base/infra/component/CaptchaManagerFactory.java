package com.digcoin.snapx.server.base.infra.component;

import com.digcoin.snapx.core.common.error.CommonError;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.SpringTemplateEngine;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.sns.SnsClient;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CaptchaManagerFactory {

    private final StringRedisTemplate redisTemplate;
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final SnsClient snsClient;
    private final PinpointClient pinpointClient;

    @Value("${app.vars.captcha.defaults.channel}")
    private String defaultCaptchaChannel;

    public CaptchaManager get(CaptchaChannel channel) {
        if (Objects.isNull(channel)) {
            channel = CaptchaChannel.of(defaultCaptchaChannel);
        }
        switch (channel) {
            case SMS:
                return new SmsCaptchaManager(this);
            case EMAIL:
                return new EmailCaptchaManager(this);
            case CONST:
                return new ConstCaptchaManager(this);
            case CONSOLE:
                return new ConsoleCaptchaManager(this);
            default:
                throw CommonError.UNEXPECT_ERROR.withDefaults();
        }
    }

    StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    JavaMailSender getMailSender() {
        return mailSender;
    }

    SpringTemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    SnsClient getSnsClient() {
        return snsClient;
    }

    PinpointClient getPinpointClient() {
        return pinpointClient;
    }

}
