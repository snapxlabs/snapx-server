package com.digcoin.snapx.domain.infra.component.discord;

import com.digcoin.snapx.domain.infra.component.discord.service.GuildService;
import com.digcoin.snapx.domain.infra.component.discord.service.OAuthService;
import com.digcoin.snapx.domain.infra.component.discord.service.UserService;
import com.digcoin.snapx.domain.infra.config.properties.DiscordOauthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordRestApi {

    final RestTemplateBuilder restTemplateBuilder;
    final DiscordOauthProperties discordOauthProperties;

    public OAuthService getOAuthService() {
        return new OAuthService(this, RestContext.empty());
    }

    public GuildService getGuildService(RestContext context) {
        return new GuildService(this, context);
    }

    public UserService getUserService(RestContext context) {
        return new UserService(this, context);
    }

}
