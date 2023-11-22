package com.digcoin.snapx.domain.restaurant.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/1 22:19
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantReviewedEvent implements Serializable {

    private Long reviewId;

}
