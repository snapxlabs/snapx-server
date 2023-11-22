package com.digcoin.snapx.domain.infra.component.discord.service;

import com.digcoin.snapx.domain.infra.component.discord.DiscordRestApi;
import com.digcoin.snapx.domain.infra.component.discord.RestComponent;
import com.digcoin.snapx.domain.infra.component.discord.RestContext;
import com.digcoin.snapx.domain.infra.component.discord.model.TokensResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
public class OAuthService extends RestComponent {

    public OAuthService(DiscordRestApi discordRestApi, RestContext context) {
        super(discordRestApi, context);
    }

    public String getAuthorizationURL(String state) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/oauth2/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", discordOauthProperties.getClientId())
                .queryParam("redirect_uri", discordOauthProperties.getRedirectUri())
                .queryParam("scope", String.join("%20", discordOauthProperties.getScope()));
        if (state != null && state.trim().length() > 0) {
            builder.queryParam("state", state);
        }
        return builder.build().toUriString();
    }

    public TokensResponse getTokens(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", discordOauthProperties.getClientId());
        requestBody.add("client_secret", discordOauthProperties.getClientSecret());
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("code", code);
        requestBody.add("redirect_uri", discordOauthProperties.getRedirectUri());
        requestBody.add("scope", String.join(" ", discordOauthProperties.getScope()));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);
        URI uri = toUri("/oauth2/token");
        return restTemplate.exchange(uri, HttpMethod.POST, httpEntity, TokensResponse.class).getBody();
    }

    public TokensResponse refreshTokens(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", discordOauthProperties.getClientId());
        requestBody.add("client_secret", discordOauthProperties.getClientSecret());
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", refreshToken);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);
        URI uri = toUri("/oauth2/token");
        return restTemplate.exchange(uri, HttpMethod.POST, httpEntity, TokensResponse.class).getBody();
    }

}
