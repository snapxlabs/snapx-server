package com.digcoin.snapx.domain.restaurant.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.digcoin.snapx.domain.restaurant.enums.ReviewChangeFromType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/10 22:33
 * @description
 */
@Data
@Accessors(chain = true)
@TableName("rst_restaurant_review_change")
public class RestaurantReviewChange implements Serializable {

    /**
     * ID
     */
    @TableId("id")
    private Long id;

    /**
     * 评价 ID
     */
    @TableField("review_id")
    private Long reviewId;

    /**
     * 变更前：评分
     */
    @TableField("before_rate")
    private Integer beforeRate;

    /**
     * 变更前：评价内容
     */
    @TableField("before_content")
    private String beforeContent;

    /**
     * 变更前：奖励金额
     */
    @TableField("before_earned")
    private BigDecimal beforeEarned;

    /**
     * 变更后：评分
     */
    @TableField("after_rate")
    private Integer afterRate;

    /**
     * 变更后：评价内容
     */
    @TableField("after_content")
    private String afterContent;

    /**
     * 变更后：奖励金额
     */
    @TableField("after_earned")
    private BigDecimal afterEarned;

    /**
     * 变更来自类型枚举：MEMBER=会员；ADMIN=管理员
     */
    @TableField("from_type")
    private ReviewChangeFromType fromType;

    /**
     * 会员 ID
     */
    @TableField("from_member_id")
    private Long fromMemberId;

    /**
     * 管理员 ID
     */
    @TableField("from_admin_id")
    private Long fromAdminId;

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
     * 创建人 ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新人 ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 删除时间，值为0则未删除
     */
    @TableField("deleted")
    @TableLogic
    private Long deleted;

    public static final String REVIEW_ID = "review_id";

}
