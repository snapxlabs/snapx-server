package com.digcoin.snapx.domain.restaurant.event;

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
public class RestaurantReviewLikedEvent implements Serializable {

    private Long reviewId;
    private Boolean likes;
    private Long fromMemberId;

    public RestaurantReviewLikedEvent(Long reviewId, Boolean likes, Long fromMemberId) {
        this.reviewId = reviewId;
        this.likes = likes;
        this.fromMemberId = fromMemberId;
    }

}
