package com.digcoin.snapx.server.app.infra.controller;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.infra.bo.InSiteMessageQuery;
import com.digcoin.snapx.domain.infra.entity.InSiteMessage;
import com.digcoin.snapx.domain.infra.service.InSiteMessageService;
import com.digcoin.snapx.server.app.infra.dto.InSiteMessageDTO;
import com.digcoin.snapx.server.app.infra.dto.InSiteMessageResult;
import com.digcoin.snapx.server.app.member.converter.InSiteMessageConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@Tag(name = "1101 - 站内信")
@RequiredArgsConstructor
@RequestMapping("/infra/in-site-message")
@RestController
public class InSiteMessageController {

    private final InSiteMessageService inSiteMessageService;
    private final InSiteMessageConverter inSiteMessageConverter;

    @Operation(description = "分页获取当前用户站内信列表")
    @GetMapping("page-in-site-message")
    public InSiteMessageResult pageInSiteMessage(@Schema(hidden = true) CurrentUser currentUser, InSiteMessageQuery query) {
        query.setToMemberId(currentUser.getId());
        Long unreadCount = inSiteMessageService.getUnreadCount(currentUser.getId());
        PageResult<InSiteMessage> result = inSiteMessageService.pageInSiteMessage(query);
        return InSiteMessageResult.create(unreadCount, PageResult.fromPageResult(result, inSiteMessageConverter::intoDTO));
    }

    @Operation(description = "获取当前用户未读站内信数量")
    @GetMapping("get-unread-count")
    public Long getUnreadCount(@Schema(hidden = true) CurrentUser currentUser) {
        return inSiteMessageService.getUnreadCount(currentUser.getId());
    }

    @Operation(description = "获取指定站内信详情并更新为已读")
    @GetMapping("find-in-site-message/{id}")
    public InSiteMessageDTO findInSiteMessage(@Schema(hidden = true) CurrentUser currentUser,
                                              @Schema(description = "站内信id") @PathVariable Long id) {
        Optional<InSiteMessage> result = Optional.ofNullable(inSiteMessageService.findInSiteMessage(id));
        if (result.isEmpty()) {
            return null;
        }
        if (!Objects.equals(currentUser.getId(), result.map(InSiteMessage::getToMemberId).orElse(null))) {
            return null;
        }
        inSiteMessageService.updateInSiteMessageRead(id);
        return result.map(inSiteMessageConverter::intoDTO).orElse(null);
    }

    @Operation(description = "更新所有未读消息为已读")
    @GetMapping("update-all-in-site-message-read")
    public void updateAllInSiteMessageRead(@Schema(hidden = true) CurrentUser currentUser) {
        Long unreadCount = inSiteMessageService.getUnreadCount(currentUser.getId());
        if (unreadCount == 0L) {
            return;
        }
        inSiteMessageService.updateAllInSiteMessageRead(currentUser.getId());
    }

}
