package com.digcoin.snapx.server.app.infra.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.infra.service.AwsSnsMobilePushService;
import com.digcoin.snapx.server.app.infra.dto.DeviceRegisterPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "1100 - 移动推送管理")
@SaCheckLogin
@RequiredArgsConstructor
@RequestMapping("/infra/pushing/aws")
@RestController
public class AwsSnsMobilePushController {

    private final AwsSnsMobilePushService awsSnsMobilePushService;

    @PostMapping("register-device")
    @Operation(summary = "注册移动推送设备")
    public void registerDevice(@Schema(hidden = true) CurrentUser currentUser,
                               @RequestBody DeviceRegisterPayload payload) {

        awsSnsMobilePushService.registerDevice(currentUser.getId(), payload.getPlatform(), payload.getDeviceToken());

    }

    @PostMapping("publish-message")
    @Operation(summary = "测试向当前用户推送消息")
    public void publishMessage(@Schema(hidden = true) CurrentUser currentUser, @RequestParam String message) {
        awsSnsMobilePushService.publish(currentUser.getId(), message);
    }


}
