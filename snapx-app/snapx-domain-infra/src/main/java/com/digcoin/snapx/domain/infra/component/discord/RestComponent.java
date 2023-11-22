package com.digcoin.snapx.domain.infra.component.discord;

import com.digcoin.snapx.domain.infra.config.properties.DiscordOauthProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;

public abstract class RestComponent {

    protected static final String BASE_URL = "https://discord.com/api";

    protected final RestTemplate restTemplate;
    protected final DiscordOauthProperties discordOauthProperties;

    protected final RestContext context;

    public RestComponent(DiscordRestApi discordRestApi, RestContext context) {
        this.restTemplate = discordRestApi.restTemplateBuilder
//                .requestFactory(() -> {
//
//                    // todo 发布时去除
//                    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1087));
//                    factory.setProxy(proxy);
//                    return factory;
//                })
                .build();
        this.discordOauthProperties = discordRestApi.discordOauthProperties;
        this.context = context;
    }

    protected URI toUri(String path) {
        return UriComponentsBuilder.fromUriString(BASE_URL)
                .path(path)
                .build()
                .toUri();
    }

}
