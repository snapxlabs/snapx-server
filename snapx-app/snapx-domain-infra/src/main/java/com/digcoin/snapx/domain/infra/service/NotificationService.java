package com.digcoin.snapx.domain.infra.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.infra.bo.NotificationQuery;
import com.digcoin.snapx.domain.infra.entity.Notification;
import com.digcoin.snapx.domain.infra.enums.NotificationStatus;
import com.digcoin.snapx.domain.infra.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;

    public PageResult<Notification> pageNotification(NotificationQuery query) {
        return PageResult.fromPage(
                notificationMapper.selectPage(
                        PageHelper.getPage(query),
                        Wrappers.lambdaQuery(Notification.class)
                                .select(Notification.class, field -> !field.getProperty().equals("memberIds")) // 列表查询排除TEXT大字段
                                .like(StringUtils.isNotBlank(query.getTitle()), Notification::getTitle, query.getTitle())
                                .like(StringUtils.isNotBlank(query.getContext()), Notification::getContext, query.getContext())
                                .eq(Objects.nonNull(query.getStatus()), Notification::getStatus, query.getStatus())
                                .orderByDesc(Notification::getCreateTime)),
                Function.identity());
    }

    public Notification findNotification(Long id) {
        return notificationMapper.selectById(id);
    }

    public Long createNotification(Notification entity) {
        if (Objects.isNull(entity.getStatus())) {
            entity.setStatus(NotificationStatus.DRAFT);
        }
        notificationMapper.insert(entity);
        return entity.getId();
    }

    public void updateNotification(Notification entity) {
        notificationMapper.updateById(entity);
    }

    public void deleteNotification(Long id) {
        notificationMapper.deleteById(id);
    }

    public void updateStatus(Long id, NotificationStatus status) {
        Notification example = new Notification();
        example.setId(id);
        example.setStatus(status);
        notificationMapper.updateById(example);
    }

}
