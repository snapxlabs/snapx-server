package com.digcoin.snapx.domain.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digcoin.snapx.domain.restaurant.bo.ReviewChangeCountBO;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewChange;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/10 22:33
 * @description
 */
@Mapper
public interface RestaurantReviewChangeMapper extends BaseMapper<RestaurantReviewChange> {

    @MapKey("reviewId")
    HashMap<Long, ReviewChangeCountBO> countByReviewIds(List<Long> reviewIds);


}
