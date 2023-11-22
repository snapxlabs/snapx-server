package com.digcoin.snapx.server.admin.infra.service;

import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.infra.bo.NotificationQuery;
import com.digcoin.snapx.domain.infra.entity.Notification;
import com.digcoin.snapx.domain.infra.enums.NotificationStatus;
import com.digcoin.snapx.domain.infra.service.InSiteMessageService;
import com.digcoin.snapx.domain.infra.service.NotificationService;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.service.MemberService;
import com.digcoin.snapx.server.admin.infra.converter.NotificationConverter;
import com.digcoin.snapx.server.admin.infra.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationAppService {

    private final NotificationConverter notificationConverter;
    private final NotificationService notificationService;
    private final MemberService memberService;
    private final InSiteMessageService inSiteMessageService;

    public PageResult<NotificationDTO> pageNotification(NotificationQuery query) {
        return PageResult.fromPageResult(notificationService.pageNotification(query), notificationConverter::intoDTO);
    }

    public NotificationDTO findNotification(Long id) {
        Function<Notification, NotificationDTO> converter = entity -> {
            NotificationDTO dto = notificationConverter.intoDTO(entity);
            if (Boolean.FALSE.equals(entity.getGlobal()) && StringUtils.isNotBlank(entity.getMemberIds())) {
                dto.setMemberIdList(Arrays.stream(entity.getMemberIds().split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList()));
            }
            return dto;
        };
        return Optional.ofNullable(notificationService.findNotification(id))
                .map(converter)
                .orElse(null);
    }

    public NotificationDTO createNotification(NotificationDTO payload) {
        Notification entity = notificationConverter.fromDTO(payload);
        if (Boolean.FALSE.equals(payload.getGlobal())) {
            if (CollectionUtils.isEmpty(payload.getMemberIdList())) {
                throw CommonError.PARAMETER_MISS_ERROR.withMessage("member id list is require while global is false");
            }
            entity.setMemberIds(payload.getMemberIdList().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")));
        }
        Long id = notificationService.createNotification(entity);
        return findNotification(id);
    }

    public NotificationDTO updateNotification(Long id, NotificationDTO payload) {
        Notification notification = notificationService.findNotification(id);
        if (Objects.isNull(notification)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("Notification not exist");
        }
        if (!NotificationStatus.DRAFT.equals(notification.getStatus())) {
            throw CommonError.PARAMETER_ERROR.withMessage("Notification can not change while status not DRAFT");
        }
        Notification entity = notificationConverter.fromDTO(payload);
        entity.setId(id);
        if (Boolean.FALSE.equals(payload.getGlobal())) {
            if (CollectionUtils.isEmpty(payload.getMemberIdList())) {
                throw CommonError.PARAMETER_MISS_ERROR.withMessage("member id list is require while global is false");
            }
            entity.setMemberIds(payload.getMemberIdList().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")));
        } else {
            entity.setMemberIds(null);
        }
        notificationService.updateNotification(entity);
        return findNotification(id);
    }

    public NotificationDTO deleteNotification(Long id) {
        NotificationDTO notification = findNotification(id);
        notificationService.deleteNotification(id);
        return notification;
    }

    public NotificationDTO sendNotification(Long id) {
        Notification entity = notificationService.findNotification(id);
        if (Objects.isNull(entity)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("Notification not exist");
        }
        if (!NotificationStatus.DRAFT.equals(entity.getStatus())) {
            throw CommonError.PARAMETER_ERROR.withMessage("Notification can not publish while status not DRAFT");
        }
        notificationService.updateStatus(id, NotificationStatus.PROCESS);
        NotificationDTO notification = findNotification(id);

        Set<Long> memberIdSet = Collections.emptySet();
        if (Boolean.TRUE.equals(notification.getGlobal())) {
            memberIdSet = memberService.listAllMember().stream()
                    .map(Member::getId)
                    .collect(Collectors.toSet());
        } else if (Boolean.FALSE.equals(notification.getGlobal())) {
            memberIdSet = memberService.listMember(notification.getMemberIdList()).stream()
                    .map(Member::getId)
                    .collect(Collectors.toSet());
        }

        if (CollectionUtils.isEmpty(memberIdSet)) {
            notificationService.updateStatus(id, NotificationStatus.DONE);
            notification.setStatus(NotificationStatus.DONE);
            return notification;
        }

        CompletableFuture<Void> future = inSiteMessageService.sendBatchNotify(memberIdSet,
                notification.getInSiteMessage(),
                notification.getPush(),
                s -> {
                    s.setTitle(notification.getTitle());
                    s.setContext(notification.getContext());
                });
        future.whenCompleteAsync((result, error) -> {
            if (Objects.nonNull(error)) {
                log.error("sendNotification fail id:[{}]", id, error);
                notificationService.updateStatus(id, NotificationStatus.FAIL);
                return;
            }
            notificationService.updateStatus(id, NotificationStatus.DONE);
        });

        return notification;
    }

}
