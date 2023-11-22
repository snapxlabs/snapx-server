package com.digcoin.snapx.server.app.member.controller;

import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.event.MemberAccessEvent;
import com.digcoin.snapx.domain.member.service.MemberPointsHistoryRankingService;
import com.digcoin.snapx.server.app.member.dto.AuthDTO;
import com.digcoin.snapx.server.app.member.dto.MemberDTO;
import com.digcoin.snapx.server.app.member.dto.MemberPasswordDTO;
import com.digcoin.snapx.server.app.member.service.MemberAppService;
import com.digcoin.snapx.server.base.infra.component.CaptchaChannel;
import com.digcoin.snapx.server.base.infra.component.CaptchaType;
import com.digcoin.snapx.server.base.member.service.MemberEventAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Tag(name = "100 - 会员 - 注册登录")
@Slf4j
@Validated
@RestController
@RequestMapping("/member/auth")
@RequiredArgsConstructor
public class MemberAuthController {

    private final MemberAppService memberAppService;
    private final MemberEventAppService memberEventAppService;
    private final MemberPointsHistoryRankingService memberPointsHistoryRankingService;

    @Operation(summary = "用户注册")
    @PostMapping("sign-up")
    public AuthDTO signUp(@RequestParam(required = false) String client, @RequestBody @Validated MemberDTO memberDTO) {
        AuthDTO token = memberAppService.createMember(memberDTO);
        if (!"h5".equals(client)) {
            issueBonus(token);
        }
        updateRanking(token);
        memberEventAppService.publishSignUpEvent(token.getMember().getId());
        return token;
    }

    @Operation(summary = "用户密码登录")
    @PostMapping("sign-in-by-password")
    public AuthDTO signInByPassword(@NotEmpty(message = "account is blank") @Schema(description = "账号") @RequestParam String account,
                                    @NotEmpty(message = "password is blank") @Schema(description = "密码", type = "string", format = "password") @RequestParam String password) {
        AuthDTO authDTO = memberAppService.signIn(account, password);
        issueBonus(authDTO);
        memberEventAppService.publishAccessEvent(authDTO.getMember().getId());
        return authDTO;
    }

    @Operation(summary = "用户验证码登录", description = "鉴权相关错误代码：<br/>11011 未能读取到有效Token<br/>" +
            "11012 Token无效<br/>" +
            "11013 Token已过期<br/>" +
            "11014 Token已被顶下线<br/>" +
            "11015 Token已被踢下线<br/>" +
            "11016 Token已临时过期")
    @PostMapping("sign-in-by-captcha")
    public AuthDTO signInByCaptcha(@NotEmpty(message = "account is blank") @Schema(description = "账号") @RequestParam String account,
                                   @Schema(hidden = true) @RequestParam(required = false) CaptchaChannel captchaChannel,
                                   @NotEmpty(message = "code is blank") @Schema(description = "验证码") @RequestParam String captcha) {
        AuthDTO authDTO = memberAppService.signIn(account, captchaChannel, captcha);
        issueBonus(authDTO);
        memberEventAppService.publishAccessEvent(authDTO.getMember().getId());
        return authDTO;
    }

    @Operation(summary = "退出登录")
    @PostMapping("logout")
    public void logout() {
        memberAppService.logout();
    }

    @Operation(summary = "忘记密码")
    @PostMapping("update-member-password")
    public AuthDTO updateMemberPassword(@RequestBody @Validated MemberPasswordDTO memberPasswordDTO) {
        memberPasswordDTO.setCaptchaType(CaptchaType.FORGOT_PASSWORD);
        return memberAppService.updateMemberPassword(memberPasswordDTO);
    }

    @Operation(summary = "更新会员最后一次访问时间")
    @PostMapping("update-member-access")
    public void updateMemberAccess(@Schema(hidden = true)CurrentUser currentUser) {
        memberEventAppService.publishAccessEvent(currentUser.getId());
    }

    @Operation(summary = "更新会员最后一次访问所在时区")
    @PostMapping("update-member-timezone")
    public void updateMemberTimezone(@Schema(hidden = true)CurrentUser currentUser,
                                     @Schema(description = "传用户所在时区，东时区用正数，西时区负数。例如东八区：timezone=8，西五区：timezone=-5") @RequestParam Integer timezone) {
        memberAppService.updateMemberTimezone(currentUser.getId(), timezone);
    }

    @Operation(summary = "删除用户账号")
    @PostMapping("delete-member")
    public void deleteMember(@Schema(hidden = true)CurrentUser currentUser) {
        memberAppService.deleteMember(currentUser.getId());
        logout();
    }

    @Operation(summary = "校验邀请码是否有效")
    @GetMapping("validate-invite-code/{code}")
    public void validateInviteCode(@Schema(description = "邀请码") @PathVariable Long code) {
        memberAppService.validateInviteCode(code);
    }

    @GetMapping("exchange-discord-oauth-code")
    public void exchangeDiscordOauthCode(@RequestParam String code) {
        log.info("{}", code);
    }

    private void issueBonus(AuthDTO token) {
        try {
            memberAppService.issueBonus(token);
        } catch (Exception e) {
            log.error("issueBonus fail token:[{}]", token, e);
        }
    }

    private void updateRanking(AuthDTO token) {
        try {
            memberPointsHistoryRankingService.updateRanking(token.getMember().getId(), LocalDateTime.now(), 0L);
        } catch (Exception e) {
            log.error("issueBonus fail token:[{}]", token, e);
        }
    }
}
