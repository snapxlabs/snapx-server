package com.digcoin.snapx.domain.restaurant.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.digcoin.snapx.domain.restaurant.enums.OrderStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 餐厅订单
 * </p>
 *
 * @author Generator
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
@TableName("rst_order")
public class Order implements Serializable {

    public static final String MEMBER_ID = "member_id";
    public static final Object EARNED = "earned";
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private Long id;

    /**
     * 国家 ID
     */
    @TableField("country_id")
    private Long countryId;

    /**
     * 地点 ID
     */
    @TableField("locality_id")
    private Long localityId;

    /**
     * 餐厅 ID
     */
    @TableField("restaurant_id")
    private Long restaurantId;

    /**
     * 会员 ID
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 状态枚举：ONGOING=进行中；CANCELED=取消；COMPLETED=完成
     */
    @TableField("status")
    private OrderStatus status;

    /**
     * 照片地址（英文逗号分割）
     */
    @TableField("photo_urls")
    private String photoUrls;

    /**
     * 评分：1-5
     */
    @TableField("rating")
    private Integer rating;

    /**
     * 花费
     */
    @TableField("spend")
    private BigDecimal spend;

    /**
     * 货币代码
     */
    @TableField("currency_code")
    private String currencyCode;

    /**
     * 评价内容
     */
    @TableField("content")
    private String content;

    /**
     * 赚取
     */
    @TableField("earned")
    private BigDecimal earned;

    /**
     * 经度
     */
    @TableField("lng")
    private BigDecimal lng;

    /**
     * 纬度
     */
    @TableField("lat")
    private BigDecimal lat;

    /**
     * 订单信息唯一标志
     */
    @TableField("identity")
    private String identity;

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
