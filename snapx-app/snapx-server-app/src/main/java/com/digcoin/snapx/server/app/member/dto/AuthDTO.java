package com.digcoin.snapx.server.app.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "令牌信息", accessMode = Schema.AccessMode.READ_ONLY)
    private TokenInfoDTO token;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "会员信息", accessMode = Schema.AccessMode.READ_ONLY)
    private MemberDTO member;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "是否第一次登录", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean loginForFirstTime;

}
