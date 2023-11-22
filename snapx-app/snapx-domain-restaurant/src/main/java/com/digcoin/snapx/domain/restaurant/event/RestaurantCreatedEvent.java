package com.digcoin.snapx.domain.restaurant.event;

import com.digcoin.snapx.core.redisson.DelayMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/20 13:24
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCreatedEvent implements DelayMessage {

    private Long restaurantId;

    @Override
    public Long getId() {
        return restaurantId;
    }
}
