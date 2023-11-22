package com.digcoin.snapx.domain.infra.service;

import com.digcoin.snapx.domain.infra.bo.ExchangeTokenBO;
import com.digcoin.snapx.domain.infra.component.discord.DiscordRestApi;
import com.digcoin.snapx.domain.infra.component.discord.RestContext;
import com.digcoin.snapx.domain.infra.component.discord.model.Guild;
import com.digcoin.snapx.domain.infra.component.discord.model.TokensResponse;
import com.digcoin.snapx.domain.infra.component.discord.model.User;
import com.digcoin.snapx.domain.infra.component.discord.service.GuildService;
import com.digcoin.snapx.domain.infra.component.discord.service.OAuthService;
import com.digcoin.snapx.domain.infra.component.discord.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordService {

    private static final String KEY_PREFIX = "SNAPX:DISCORD:TOKEN";
    private final DiscordRestApi discordRestApi;
    private final StringRedisTemplate redisTemplate;

    public String getUrl(String state) {
        OAuthService oauthService = discordRestApi.getOAuthService();
        return oauthService.getAuthorizationURL(state);
    }

    public ExchangeTokenBO exchangeToken(String code) {
        OAuthService oauthService = discordRestApi.getOAuthService();
        TokensResponse tokens = oauthService.getTokens(code);
        User user = getUser(tokens);
        cacheTokens(user.getId(), tokens);
        ExchangeTokenBO exchangeTokenBO = new ExchangeTokenBO();
        exchangeTokenBO.setTokens(tokens);
        exchangeTokenBO.setUser(user);
        return exchangeTokenBO;
    }

    public User getUser(TokensResponse tokens) {
        RestContext context = RestContext.from(tokens);
        UserService userService = discordRestApi.getUserService(context);
        User user = userService.getCurrentUser();
        return user;
    }

    public void addGuildMember(TokensResponse tokens, String guildId, String userId) {
        RestContext context = RestContext.from(tokens);
        GuildService guildService = discordRestApi.getGuildService(context);
        guildService.addGuildMember(guildId, userId);
    }

    public List<Guild> listGuild(TokensResponse tokens) {
        RestContext context = RestContext.from(tokens);
        UserService userService = discordRestApi.getUserService(context);
        return userService.getCurrentUserGuilds();
    }

    public TokensResponse getTokens(String userId) {
        String key = buildKey(userId);
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            return null;
        }
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
        TokensResponse tokensResponse = new TokensResponse();
        tokensResponse.setAccessToken(hashOps.get("access_token"));
        tokensResponse.setRefreshToken(hashOps.get("refresh_token"));
        tokensResponse.setTokenType(hashOps.get("token_type"));
        tokensResponse.setScope(hashOps.get("scope"));
        tokensResponse.setExpiresIn(Optional.ofNullable(hashOps.get("expires_in")).map(Integer::valueOf).orElse(0));

        boolean expires = true;
        String createTime = hashOps.get("create_time");
        if (StringUtils.isNotBlank(createTime)) {
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime starTime = LocalDateTime.parse(createTime, pattern);
            LocalDateTime endTime = starTime.plusSeconds(tokensResponse.getExpiresIn());
            Duration between = Duration.between(LocalDateTime.now(), endTime);
            if (between.toMinutes() > 1) {
                expires = false;
            }
        }

        if (expires && StringUtils.isNotBlank(tokensResponse.getRefreshToken())) {
            OAuthService oauthService = discordRestApi.getOAuthService();
            tokensResponse = oauthService.refreshTokens(tokensResponse.getRefreshToken());
            cacheTokens(userId, tokensResponse);
            return tokensResponse;
        } else {
            return null;
        }
    }

    private void cacheTokens(String userId, TokensResponse tokens) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String key = buildKey(userId);
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
        redisTemplate.expire(key, tokens.getExpiresIn(), TimeUnit.SECONDS);
        hashOps.put("access_token", tokens.getAccessToken());
        hashOps.put("refresh_token", tokens.getRefreshToken());
        hashOps.put("expires_in", String.valueOf(tokens.getExpiresIn()));
        hashOps.put("token_type", tokens.getTokenType());
        hashOps.put("scope", tokens.getScope());
        hashOps.put("create_time", pattern.format(LocalDateTime.now()));
    }

    private String buildKey(String userId) {
        return String.join(":", KEY_PREFIX, userId);
    }

}
