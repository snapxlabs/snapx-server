package com.digcoin.snapx.server.app.member.config;

import com.digcoin.snapx.core.redisson.BlockingQueueManager;
import com.digcoin.snapx.core.redisson.DelayMessageManager;
import com.digcoin.snapx.domain.member.event.MemberAccessEvent;
import com.digcoin.snapx.domain.member.event.MemberSignUpEvent;
import com.digcoin.snapx.domain.restaurant.constant.QueueConst;
import com.digcoin.snapx.domain.restaurant.event.OrderCompletedEvent;
import com.digcoin.snapx.domain.restaurant.event.RestaurantCreatedEvent;
import com.digcoin.snapx.domain.restaurant.event.RestaurantReviewLikedEvent;
import com.digcoin.snapx.domain.restaurant.event.RestaurantReviewedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    @Bean(name = "memberAccessEventQueue", initMethod = "init", destroyMethod = "destroy")
    public BlockingQueueManager<MemberAccessEvent> memberAccessEventQueue() {
        return new BlockingQueueManager<>("memberAccessEventQueue", redissonClient, new JsonJacksonCodec(objectMapper), true);
    }

    @Bean(name = "memberSignUpEventQueue", initMethod = "init", destroyMethod = "destroy")
    public BlockingQueueManager<MemberSignUpEvent> memberSignUpEventQueue() {
        return new BlockingQueueManager<>("memberSignUpEventQueue", redissonClient, new JsonJacksonCodec(objectMapper),true);
    }

    @Bean(name = QueueConst.ORDER_COMPLETED_EVENT_QUEUE, initMethod = "init", destroyMethod = "destroy")
    public BlockingQueueManager<OrderCompletedEvent> orderCompletedEventQueue() {
        return new BlockingQueueManager<>(QueueConst.ORDER_COMPLETED_EVENT_QUEUE, redissonClient, new JsonJacksonCodec(objectMapper),true);
    }

    @Bean(name = QueueConst.RESTAURANT_REVIEWED_QUEUE, initMethod = "init", destroyMethod = "destroy")
    public BlockingQueueManager<RestaurantReviewedEvent> restaurantReviewedEventQueue() {
        return new BlockingQueueManager<>(QueueConst.RESTAURANT_REVIEWED_QUEUE, redissonClient, new JsonJacksonCodec(objectMapper),true);
    }

    @Bean(name = QueueConst.RESTAURANT_REVIEW_LIKED_EVENT_QUEUE, initMethod = "init", destroyMethod = "destroy")
    public BlockingQueueManager<RestaurantReviewLikedEvent> restaurantReviewLikedEventQueue() {
        return new BlockingQueueManager<>(QueueConst.RESTAURANT_REVIEW_LIKED_EVENT_QUEUE, redissonClient, new JsonJacksonCodec(objectMapper),true);
    }

    @Bean(name = QueueConst.RESTAURANT_CREATED_EVENT_QUEUE, initMethod = "init", destroyMethod = "destroy")
    public DelayMessageManager<RestaurantCreatedEvent> restaurantCreatedEventQueue() {
        return new DelayMessageManager<>(QueueConst.RESTAURANT_CREATED_EVENT_QUEUE, id -> new RestaurantCreatedEvent(id), redissonClient, true);
    }

}
