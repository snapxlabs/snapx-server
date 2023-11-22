package com.digcoin.snapx.domain.restaurant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.redisson.BlockingQueueManager;
import com.digcoin.snapx.domain.restaurant.bo.LatLngDTO;
import com.digcoin.snapx.domain.restaurant.constant.QueueConst;
import com.digcoin.snapx.domain.restaurant.entity.Order;
import com.digcoin.snapx.domain.restaurant.enums.OrderStatus;
import com.digcoin.snapx.domain.restaurant.error.OrderError;
import com.digcoin.snapx.domain.restaurant.event.OrderCompletedEvent;
import com.digcoin.snapx.domain.restaurant.mapper.OrderMapper;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/28 15:43
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;

    @Qualifier(QueueConst.ORDER_COMPLETED_EVENT_QUEUE)
    private final BlockingQueueManager<OrderCompletedEvent> orderCompletedEventQueue;


    public void createOrder(Order order) {
        BigDecimal earned = calcOrderCommission(order);
        order.setEarned(earned);
        orderMapper.insert(order);
    }

    public String getIdentity(LatLngDTO latLng, String photoUrls, Integer rating, BigDecimal spend, String currencyCode, String content) {
        Object[] objects = {latLng.getLat(), latLng.getLng(), photoUrls, rating, spend, content, currencyCode};
        String identity = Joiner.on(",").useForNull("null").join(objects);
        String md5 = DigestUtils.md5DigestAsHex(identity.getBytes());
        log.info("OrderService.getIdentity: {} -> {}", identity, md5);
        return md5;
    }

    public boolean existsByIdentity(String identity) {
        return orderMapper.selectCount(Wrappers.<Order>lambdaQuery()
                .eq(Order::getIdentity, identity)
        ) > 0;
    }

    public void publishOrderCompletedEvent(Long orderId) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                orderCompletedEventQueue.publishEvent(new OrderCompletedEvent(orderId));
            }
        });

    }

    public void complete(Order order) {
        order.setStatus(OrderStatus.COMPLETED);
        orderMapper.updateById(order);

        this.publishOrderCompletedEvent(order.getId());
    }

    public BigDecimal calcOrderCommission(Order order) {
        String photoUrls = Optional.ofNullable(order.getPhotoUrls()).orElse("");
        String[] strings = photoUrls.split(",");
        BigDecimal num = new BigDecimal(strings.length);
        return num.multiply(new BigDecimal(5));
    }

    public BigDecimal countTotalEarned(Long memberId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq(Order.MEMBER_ID, memberId);
        wrapper.select(String.format("sum(%s)", Order.EARNED));
        BigDecimal totalEarned = (BigDecimal) orderMapper.selectObjs(wrapper).get(0);
        return Optional.ofNullable(totalEarned).orElse(BigDecimal.ZERO);
    }

    public Order findByIdOrFail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (Objects.isNull(order)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("OrderId not found");
        }
        return order;
    }

    public void updateEarned(Long orderId, BigDecimal earned) {
        Order order = findByIdOrFail(orderId);
        updateEarned(order, earned);
    }

    public void deleteById(Long orderId) {
        orderMapper.deleteById(orderId);
    }

    public void updateEarned(Order order, BigDecimal earned) {
        order.setEarned(earned);
        orderMapper.updateById(order);
    }

    public int updateNewLocalityId(Long oldLocalityId, Long newLocalityId) {
        List<Order> reviews = listByLocality(oldLocalityId);
        if (reviews.isEmpty()) {
            return 0;
        }

        LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Order::getLocalityId, oldLocalityId);
        updateWrapper.set(Order::getLocalityId, newLocalityId);
        return orderMapper.update(null, updateWrapper);
    }

    public List<Order> listByLocality(Long localityId) {
        if (Objects.isNull(localityId)) {
            return Collections.emptyList();
        }
        return orderMapper.selectList(Wrappers.<Order>lambdaQuery().eq(Order::getLocalityId, localityId));
    }

    public String getOrderIdentityOrFail(LatLngDTO latLng, String photoUrls, Integer rating, BigDecimal spend, String currencyCode, String content) {
        // 订单是否已存在
        String identity = getIdentity(latLng, photoUrls, rating, spend, currencyCode, content);
        boolean exists = existsByIdentity(identity);
        if (exists) {
            throw OrderError.ORDER_EXISTS.withDefaults();
        }
        return identity;
    }

}
