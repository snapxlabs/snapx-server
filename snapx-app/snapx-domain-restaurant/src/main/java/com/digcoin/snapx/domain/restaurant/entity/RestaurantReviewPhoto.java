package com.digcoin.snapx.domain.restaurant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 餐厅订单 - 照片
 * </p>
 *
 * @author Generator
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
@TableName("rst_restaurant_review_photo")
public class RestaurantReviewPhoto implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String MEMBER_ID = "member_id";

    /**
     * ID
     */
    @TableId("id")
    private Long id;

    /**
     * 餐厅 ID
     */
    @TableField("restaurant_id")
    private Long restaurantId;

    /**
     * 评价 ID
     */
    @TableField("review_id")
    private Long reviewId;

    /**
     * 会员 ID
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 照片 URL
     */
    @TableField("photo_url")
    private String photoUrl;

    /**
     * 照片文件 ID
     */
    @TableField("file_id")
    private Long fileId;

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


}
