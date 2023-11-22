package com.digcoin.snapx.server.app.member.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "app.member.ex-pass")
public class MemberExPassProperties {

    private List<String> accounts;

    private String passCode;

}
