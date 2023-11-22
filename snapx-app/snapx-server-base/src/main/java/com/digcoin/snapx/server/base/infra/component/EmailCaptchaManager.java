package com.digcoin.snapx.server.base.infra.component;

import com.digcoin.snapx.core.common.error.CommonError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EmailCaptchaManager extends AbstractCaptchaManager implements CaptchaManager {

    public EmailCaptchaManager(CaptchaManagerFactory captchaManagerFactory) {
        super(captchaManagerFactory);
    }

    @Override
    protected CaptchaChannel getCaptchaChannel() {
        return CaptchaChannel.EMAIL;
    }

    @Override
    protected void doSend(CaptchaType type, String receiver, String code) {
        JavaMailSender mailSender = captchaManagerFactory.getMailSender();

        Context context = new Context();
        Map<String, Object> emailParam = new HashMap<>();
        emailParam.put("tips", getTips(type));
        emailParam.put("code", code);
        context.setVariable("emailParam", emailParam);
        String emailTemplate = captchaManagerFactory.getTemplateEngine().process("email-captcha-template", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(receiver);
            helper.setFrom(new InternetAddress("hello@snapxlabs.com", "SNAPX"));
            helper.setSubject(getSubject(type));
            helper.setText(emailTemplate, true);
            mailSender.send(message);

        } catch (Exception e) {
            log.error("发送邮件失败", e);
            throw CommonError.UNEXPECT_ERROR.withCause(e);
        }
    }

    private String getTips(CaptchaType type) {
        switch (type) {
            case SIGN_IN:
                return "Login Verification";
            case SIGN_UP:
                return "Register Verification";
            case MODIFY_PASSWORD:
                return "Modify Password Verification";
            case FORGOT_PASSWORD:
                return "SNAPX Forgot Passwrod Verification";
            default:
                return "Login Verification";
        }
    }

    private String getSubject(CaptchaType type) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = formatter.format(LocalDateTime.now());
        switch (type) {
            case SIGN_IN:
                return String.format("SNAPX Login Verification %s(UTC)", now);
            case SIGN_UP:
                return String.format("SNAPX Register Verification %s(UTC)", now);
            case MODIFY_PASSWORD:
                return String.format("SNAPX Modify Password Verification %s(UTC)", now);
            case FORGOT_PASSWORD:
                return String.format("SNAPX Forgot Passwrod Verification %s(UTC)", now);
            default:
                return String.format("SNAPX Login Verification %s(UTC)", now);
        }
    }

}
