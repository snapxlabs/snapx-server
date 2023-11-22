package com.digcoin.snapx.server.app.member.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.infra.service.PhoneAreaCodeService;
import com.digcoin.snapx.server.app.member.dto.CaptchaValidateDTO;
import com.digcoin.snapx.server.app.member.service.MemberAppService;
import com.digcoin.snapx.server.base.infra.component.CaptchaChannel;
import com.digcoin.snapx.server.base.infra.component.CaptchaType;
import com.digcoin.snapx.server.base.infra.dto.CaptchaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@Tag(name = "101 - 会员 - 验证码")
@Slf4j
@Validated
@RestController
@RequestMapping("/member/captcha")
@RequiredArgsConstructor
public class MemberCaptchaController {
    private final MemberAppService memberAppService;

    @Operation(summary = "发送注册验证码")
    @PostMapping("send-sign-up-code")
    public CaptchaDTO sendSignUpCode(@NotEmpty(message = "receiver is blank") @Schema(description = "接收邮箱") @RequestParam String receiver,
                                     @Schema(hidden = true) @RequestParam(required = false) CaptchaChannel captchaChannel) {
        return memberAppService.sendSignUpCode(captchaChannel, receiver);
    }

    @Operation(summary = "校验注册验证码")
    @PostMapping("verify-sign-up-code")
    public void sendSignUpCode(@RequestBody CaptchaValidateDTO captchaValidateDTO) {
        captchaValidateDTO.setCaptchaType(CaptchaType.SIGN_UP);
        memberAppService.validCaptcha(captchaValidateDTO);
    }

    @Operation(summary = "发送登录验证码")
    @PostMapping("send-sign-in-code")
    public CaptchaDTO sendSignInCode(@NotEmpty(message = "receiver is blank") @Schema(description = "接收邮箱") @RequestParam String receiver,
                                           @Schema(hidden = true) @RequestParam(required = false) CaptchaChannel captchaChannel) {
        return memberAppService.sendSignInCode(captchaChannel, receiver);
    }

    @Operation(summary = "发送忘记密码验证码")
    @PostMapping("send-forgot-password-code")
    public CaptchaDTO sendForgotPasswordCode(@NotEmpty(message = "receiver is blank") @Schema(description = "接收邮箱") @RequestParam String receiver,
                                             @Schema(hidden = true) @RequestParam(required = false) CaptchaChannel captchaChannel) {
        return memberAppService.sendForgotPasswordCode(captchaChannel, receiver);
    }

    @SaCheckLogin
    @Operation(summary = "发送修改密码验证码")
    @PostMapping("send-modify-password-code")
    public CaptchaDTO sendModifyPasswordCode(@Schema(hidden = true) CurrentUser currentUser,
                                       @NotEmpty(message = "receiver is blank") @Schema(description = "接收邮箱") @RequestParam String receiver,
                                       @Schema(hidden = true) @RequestParam(required = false) CaptchaChannel captchaChannel) {
        return memberAppService.sendModifyPasswordCode(captchaChannel, currentUser.getUsername(), receiver);
    }

    @SaCheckLogin
    @Operation(summary = "发送绑定手机号验证码")
    @PostMapping("send-bind-phone-code")
    public CaptchaDTO sendBindPhoneCode(@NotEmpty(message = "area code is blank") @Schema(description = "接收手机号区域代码") @RequestParam String areaCode,
                                        @NotEmpty(message = "receiver is blank") @Schema(description = "接收手机号") @RequestParam String phone) {
        return memberAppService.sendBindPhoneCode(CaptchaChannel.SMS, areaCode, phone);
    }

}
