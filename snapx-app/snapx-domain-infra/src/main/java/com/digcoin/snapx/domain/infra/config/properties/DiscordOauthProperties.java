package com.digcoin.snapx.domain.infra.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.oauth.discord")
public class DiscordOauthProperties {

    private String clientId;

    private String clientSecret;

    private String redirectUri;

    private String[] scope;

    private String botToken;

    private String officialGuildId;

    private String resultUri;

}
