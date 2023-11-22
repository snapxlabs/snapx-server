package com.digcoin.snapx.domain.infra.component.discord;

import com.digcoin.snapx.domain.infra.component.discord.model.TokensResponse;
import org.springframework.http.HttpHeaders;

public class RestContext {

    private final TokensResponse tokens;

    private RestContext(TokensResponse tokens) {
        this.tokens = tokens;
    }

    public static RestContext empty() {
        return new RestContext(new TokensResponse());
    }

    public static RestContext from(TokensResponse tokensResponse) {
        return new RestContext(tokensResponse);
    }

    public void handleHeader(HttpHeaders headers) {
        headers.set("Authorization", "Bearer " + tokens.getAccessToken());
    }

    public void asBot(HttpHeaders headers, String token) {
        headers.set("Authorization", "Bot " + token);
    }

    public String getAccessToken() {
        return tokens.getAccessToken();
    }

}
