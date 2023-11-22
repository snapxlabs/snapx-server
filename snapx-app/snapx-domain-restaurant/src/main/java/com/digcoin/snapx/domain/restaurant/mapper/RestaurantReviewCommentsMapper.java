package com.digcoin.snapx.domain.restaurant.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewComments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/24 23:15
 * @description
 */
@Mapper
public interface RestaurantReviewCommentsMapper extends BaseMapper<RestaurantReviewComments> {

    @Select("SELECT rrrc.*, mm.account AS fromMemberAccount, " +
            "mm.nickname AS fromMemberNickName FROM `rst_restaurant_review_comments` AS rrrc " +
            "LEFT JOIN `mem_member` AS mm ON mm.id = rrrc.from_member_id " +
            "${ew.customSqlSegment}")
    IPage<RestaurantReviewComments> pageReviewComments(IPage<RestaurantReviewComments> page,@Param(Constants.WRAPPER) QueryWrapper<RestaurantReviewComments> queryWrapper);
}
