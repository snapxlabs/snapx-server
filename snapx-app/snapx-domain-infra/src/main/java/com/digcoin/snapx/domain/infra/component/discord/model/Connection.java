package com.digcoin.snapx.domain.infra.component.discord.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Connection {

    private String id;
    private String name;
    private String type;
    private boolean verified;
    @JsonProperty("friend_sync")
    private boolean friendSync;
    @JsonProperty("show_activity")
    private boolean showActivity;
    private int visibility;

}
