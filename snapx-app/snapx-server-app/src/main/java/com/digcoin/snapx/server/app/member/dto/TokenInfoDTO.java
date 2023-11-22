package com.digcoin.snapx.server.app.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TokenInfoDTO {

    @Schema(description = "token值")
    private String tokenValue;

    @Schema(description = "token剩余有效期 (单位: 秒)", type = "int64")
    private Long tokenTimeout;

}
