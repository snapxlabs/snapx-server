package com.digcoin.snapx.domain.infra.component.discord.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {

    private String id;
    private String username;
    private String avatar;
    private String discriminator;
    private Boolean bot;
    private Boolean system;
    @JsonProperty("mfa_enabled")
    private Boolean mfaEnabled;
    private String locale;
    private Boolean verified;
    private String email;
    private Long flags;
    @JsonProperty("premium_type")
    private Integer premiumType;

}
