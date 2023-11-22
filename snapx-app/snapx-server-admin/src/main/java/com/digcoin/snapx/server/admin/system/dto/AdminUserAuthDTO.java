package com.digcoin.snapx.server.admin.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminUserAuthDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "用户详细信息", accessMode = Schema.AccessMode.READ_ONLY)
    private AdminUserDTO userDetail;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "token详细信息", accessMode = Schema.AccessMode.READ_ONLY)
    private TokenInfoDTO tokenInfo;

}
