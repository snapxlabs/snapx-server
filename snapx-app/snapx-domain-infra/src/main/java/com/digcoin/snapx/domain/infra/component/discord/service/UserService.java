package com.digcoin.snapx.domain.infra.component.discord.service;

import com.digcoin.snapx.domain.infra.component.discord.DiscordRestApi;
import com.digcoin.snapx.domain.infra.component.discord.RestComponent;
import com.digcoin.snapx.domain.infra.component.discord.RestContext;
import com.digcoin.snapx.domain.infra.component.discord.model.Connection;
import com.digcoin.snapx.domain.infra.component.discord.model.Guild;
import com.digcoin.snapx.domain.infra.component.discord.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.List;

@Slf4j
public class UserService extends RestComponent {

    public UserService(DiscordRestApi discordRestApi, RestContext context) {
        super(discordRestApi, context);
    }

    public User getCurrentUser() {
        HttpHeaders headers = new HttpHeaders();
        context.handleHeader(headers);
        URI uri = toUri("/users/@me");
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(uri, HttpMethod.GET, entity, User.class).getBody();
    }

    public List<Guild> getCurrentUserGuilds() {
        HttpHeaders headers = new HttpHeaders();
        context.handleHeader(headers);
        URI uri = toUri("/users/@me/guilds");
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Guild>>() {}).getBody();
    }

    public List<Connection> getCurrentUserConnections() {
        HttpHeaders headers = new HttpHeaders();
        context.handleHeader(headers);
        URI uri = toUri("/users/@me/connections");
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Connection>>() {}).getBody();
    }

}
