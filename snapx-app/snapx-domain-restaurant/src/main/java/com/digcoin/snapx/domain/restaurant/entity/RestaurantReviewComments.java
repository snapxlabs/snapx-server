package com.digcoin.snapx.domain.restaurant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/22 12:34
 * @description
 */
@Data
@Accessors(chain = true)
@TableName("rst_restaurant_review_comments")
public class RestaurantReviewComments {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 评论Id
     */
    private Long reviewId;

    /**
     * 父级留言Id
     */
    private Long parentCommentsId;

    /**
     * 冗余父级留言的会员Id
     */
    private Long parentMemberId;

    /**
     * 评论者Id
     */
    private Long fromMemberId;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建操作人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新操作人id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 删除时间，值为0则未删除
     */
    private Long deleted;


    @TableField(exist = false)
    private String fromMemberNickName;


    @TableField(exist = false)
    private String fromMemberAccount;

}
