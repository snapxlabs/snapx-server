package com.digcoin.snapx.domain.infra.component.discord.service;

import com.digcoin.snapx.domain.infra.component.discord.DiscordRestApi;
import com.digcoin.snapx.domain.infra.component.discord.RestComponent;
import com.digcoin.snapx.domain.infra.component.discord.RestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GuildService extends RestComponent {

    public GuildService(DiscordRestApi discordRestApi, RestContext context) {
        super(discordRestApi, context);
    }

    public void addGuildMember(String guildId, String userId) {
        HttpHeaders headers = new HttpHeaders();
        context.handleHeader(headers);
        context.asBot(headers, discordOauthProperties.getBotToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("access_token", context.getAccessToken());

        URI uri = toUri(String.format("/guilds/%s/members/%s", guildId, userId));
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange(uri, HttpMethod.PUT, entity, Void.class);
    }

}
