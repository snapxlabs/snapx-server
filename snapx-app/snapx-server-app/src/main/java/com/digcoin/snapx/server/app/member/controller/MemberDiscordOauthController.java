package com.digcoin.snapx.server.app.member.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.error.BusinessException;
import com.digcoin.snapx.core.error.ErrorCodeDefinition;
import com.digcoin.snapx.core.web.ApiBody;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.infra.config.properties.DiscordOauthProperties;
import com.digcoin.snapx.server.app.member.service.MemberDiscordAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Tag(name = "102 - 会员 - Discord授权")
@Slf4j
@Validated
@RestController
@RequestMapping("/member/oauth/discord")
@RequiredArgsConstructor
public class MemberDiscordOauthController {

    private final MemberDiscordAppService memberDiscordAppService;
    private final DiscordOauthProperties discordOauthProperties;

    @SaCheckLogin
    @Operation(description = "获取授权并且绑定当前用户的跳转链接")
    @GetMapping("get-bind-url")
    public ApiBody<String> getBindUrl(@Schema(hidden = true) CurrentUser currentUser) {
        return ApiBody.of(memberDiscordAppService.getUrl(currentUser.getId().toString()));
    }

    @GetMapping("bind")
    public RedirectView bind(@RequestParam String code, @RequestParam(value = "state", required = false) String memberId) {
        RedirectView redirectView = new RedirectView();
        String template = "%s?type=discord&code=%s&message=%s";
        try {
            memberDiscordAppService.bind(code, Optional.ofNullable(memberId).map(Long::valueOf).orElse(null));
            redirectView.setUrl(String.format(template, discordOauthProperties.getResultUri(), "0", "success"));
        } catch (BusinessException e) {
            redirectView.setUrl(String.format(template, discordOauthProperties.getResultUri(), e.getCode(), e.getMessage()));
        } catch (Exception e) {
            ErrorCodeDefinition errorCode = CommonError.UNEXPECT_ERROR.getErrorCode();
            redirectView.setUrl(String.format(template, discordOauthProperties.getResultUri(), errorCode.getCode(), errorCode.getMessage()));
        }
        return redirectView;
    }

}
