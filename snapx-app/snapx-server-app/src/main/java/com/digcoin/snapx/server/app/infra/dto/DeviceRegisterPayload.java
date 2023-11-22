package com.digcoin.snapx.server.app.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeviceRegisterPayload {

    @Schema(description = "应用平台枚举：SNAPX_APP_IOS 苹果端； SNAPX_APP_ANDROID 安卓端")
    private String platform;

    @Schema(description = "设备token或者id")
    private String deviceToken;
}
