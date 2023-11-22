package com.digcoin.snapx.domain.restaurant.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/2 10:42
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedEvent {

    private Long orderId;

}
