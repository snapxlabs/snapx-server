package com.digcoin.snapx.server.admin.infra.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.infra.bo.NotificationQuery;
import com.digcoin.snapx.server.admin.infra.dto.NotificationDTO;
import com.digcoin.snapx.server.admin.infra.service.NotificationAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@SaCheckLogin
@Tag(name = "推送管理")
@RequiredArgsConstructor
@RequestMapping("/infra/notification")
@RestController
public class NotificationController {

    private final NotificationAppService notificationAppService;

    @GetMapping("page-notification")
    @Operation(description = "分页获取推送列表")
    public PageResult<NotificationDTO> pageNotification(NotificationQuery query) {
        return notificationAppService.pageNotification(query);
    }

    @GetMapping("find-notification/{id}")
    @Operation(description = "通过id获取指定推送详情")
    public NotificationDTO findNotification(@Schema(description = "推送id") @PathVariable Long id) {
        return notificationAppService.findNotification(id);
    }

    @PostMapping("create-notification")
    @Operation(description = "创建推送")
    public NotificationDTO createNotification(@Validated @RequestBody NotificationDTO payload) {
        return notificationAppService.createNotification(payload);
    }

    @PostMapping("update-notification/{id}")
    @Operation(description = "修改推送")
    public NotificationDTO updateNotification(@Schema(description = "推送id") @PathVariable Long id,
                                              @Validated @RequestBody NotificationDTO payload) {
        return notificationAppService.updateNotification(id, payload);
    }

    @PostMapping("delete-notification/{id}")
    @Operation(description = "删除推送")
    public NotificationDTO deleteNotification(@Schema(description = "推送id") @PathVariable Long id) {
        return notificationAppService.deleteNotification(id);
    }

    @PostMapping("send-notification/{id}")
    @Operation(description = "发送推送")
    public NotificationDTO sendNotification(@Schema(description = "推送id") @PathVariable Long id) {
        return notificationAppService.sendNotification(id);
    }

}
