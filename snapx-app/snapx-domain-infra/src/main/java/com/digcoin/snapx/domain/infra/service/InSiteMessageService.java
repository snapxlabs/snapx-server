package com.digcoin.snapx.domain.infra.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.infra.bo.InSiteMessageQuery;
import com.digcoin.snapx.domain.infra.constant.InSiteMessageStatus;
import com.digcoin.snapx.domain.infra.entity.InSiteMessage;
import com.digcoin.snapx.domain.infra.enums.InSiteMessageSubject;
import com.digcoin.snapx.domain.infra.mapper.InSiteMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InSiteMessageService {

    private final InSiteMessageMapper inSiteMessageMapper;
    private final AwsSnsMobilePushService awsSnsMobilePushService;

    public PageResult<InSiteMessage> pageInSiteMessage(InSiteMessageQuery query) {
        IPage<InSiteMessage> page = inSiteMessageMapper.selectPage(PageHelper.getPage(query),
                Wrappers.lambdaQuery(InSiteMessage.class)
                        .eq(Objects.nonNull(query.getToMemberId()), InSiteMessage::getToMemberId, query.getToMemberId())
                        .eq(Objects.nonNull(query.getSubject()), InSiteMessage::getSubject, query.getSubject())
                        .eq(Objects.nonNull(query.getStatus()), InSiteMessage::getStatus, query.getStatus())
                        .like(StringUtils.isNotBlank(query.getSearch()), InSiteMessage::getContext, query.getSearch())
                        .orderByDesc(InSiteMessage::getCreateTime)
        );
        return PageResult.fromPage(page, Function.identity());
    }

    public Long getUnreadCount(Long memberId) {
        return inSiteMessageMapper.selectCount(Wrappers.lambdaQuery(InSiteMessage.class)
                .eq(InSiteMessage::getToMemberId, memberId)
                .eq(InSiteMessage::getStatus, InSiteMessageStatus.UNREAD)
        );
    }

    public InSiteMessage findInSiteMessage(Long id) {
        return inSiteMessageMapper.selectById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateInSiteMessageRead(Long id) {
        InSiteMessage example = new InSiteMessage();
        example.setId(id);
        example.setStatus(InSiteMessageStatus.READ);
        inSiteMessageMapper.updateById(example);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateAllInSiteMessageRead(Long memberId) {
        inSiteMessageMapper.update(new InSiteMessage(), Wrappers.lambdaUpdate(InSiteMessage.class)
                .set(InSiteMessage::getStatus, InSiteMessageStatus.READ)
                .eq(InSiteMessage::getStatus, InSiteMessageStatus.UNREAD)
                .eq(InSiteMessage::getToMemberId, memberId));
    }

    /**
     * 发送站内信通知：餐馆评价被点赞
     *
     * @param toMemberId 通知对象memberId
     * @param nickname   点赞用户昵称
     * @param id         餐馆评价id
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendThumbsUpNotify(Long toMemberId, String nickname, Long id) {
        sendInSiteMessage(toMemberId, InSiteMessageSubject.THUMBS_UP, String.valueOf(id), nickname);
    }

    /**
     * 发送站内信通知：餐馆评价新回复通知
     *
     * @param toMemberId 通知对象memberId
     * @param nickname   回复用户昵称
     * @param id         回复记录id
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendNewComment(Long toMemberId, String nickname, Long id) {
        sendInSiteMessage(toMemberId, InSiteMessageSubject.NEW_COMMENT, String.valueOf(id), nickname);
    }

    /**
     * 发送站内信通知：邀请码被使用通知
     *
     * @param toMemberId 通知对象memberId
     * @param id         被邀请用户memberId
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendFriendSignedUp(Long toMemberId, Long id) {
        sendInSiteMessage(toMemberId, InSiteMessageSubject.FRIEND_SIGNED_UP, String.valueOf(id));
    }

    public CompletableFuture<Void> sendBatchNotify(Collection<Long> memberIds, boolean inSiteMessage, boolean notification, Consumer<InSiteMessage> setup) {
        return CompletableFuture.runAsync(() -> {
            if (CollectionUtils.isEmpty(memberIds)) {
                return;
            }

            List<InSiteMessage> messageList = memberIds.stream().map(memberId -> {
                InSiteMessage entity = new InSiteMessage();
                entity.setSubject(InSiteMessageSubject.BATCH_NOTIFICATION);
                entity.setToMemberId(memberId);
                entity.setStatus(InSiteMessageStatus.UNREAD);
                setup.accept(entity);
                return entity;
            }).collect(Collectors.toList());

            if (inSiteMessage) {
                for (InSiteMessage message : messageList) {
                    inSiteMessageMapper.insert(message);
                }
            }

            if (notification) {
                for (InSiteMessage message : messageList) {
                    try {
                        log.info("sendBatchNotify push to memberId:[{}] context:[{}]", message.getToMemberId(), message.getContext());
                        Map<String, Object> data = new HashMap<>();
                        awsSnsMobilePushService.publish(message.getToMemberId(), message.getContext(), data);
                    } catch (Exception e) {
                        log.error("sendBatchNotify push fail message:[{}]", message, e);
                    }
                }
            }
        });
    }

    private void sendInSiteMessage(Long toMemberId, InSiteMessageSubject subject, String relateId, String... params) {
        String title = subject.getTitle();
        String context = subject.getContext(params);

        InSiteMessage entity = new InSiteMessage();
        entity.setSubject(subject);
        entity.setToMemberId(toMemberId);
        entity.setTitle(title);
        entity.setContext(context);
        entity.setStatus(InSiteMessageStatus.UNREAD);
        entity.setRelateId(relateId);

        inSiteMessageMapper.insert(entity);
        try {
            log.info("push to memberId:[{}] context:[{}]", toMemberId, context);
            Map<String, Object> data = new HashMap<>();
            data.put("subject", subject);
            data.put("relateId", relateId);
            awsSnsMobilePushService.publish(toMemberId, context, data);
        } catch (Exception e) {
            log.error("sendInSiteMessage push fail toMemberId:[{}] subject:[{}]", toMemberId, subject, e);
        }
    }

}
