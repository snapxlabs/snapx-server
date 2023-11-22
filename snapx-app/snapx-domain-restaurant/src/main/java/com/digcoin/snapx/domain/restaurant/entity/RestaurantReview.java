package com.digcoin.snapx.domain.restaurant.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.digcoin.snapx.domain.restaurant.enums.Source;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 餐厅评价
 * </p>
 *
 * @author Generator
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
@TableName("rst_restaurant_review")
public class RestaurantReview implements Serializable {

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
     * 会员 ID
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 餐厅 ID
     */
    @TableField("restaurant_id")
    private Long restaurantId;

    /**
     * 订单 ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 评分
     */
    @TableField("rate")
    private Integer rate;

    /**
     * 花费
     */
    @TableField("spent")
    private BigDecimal spent;

    /**
     * 花费
     */
    @TableField("spent_usd")
    private BigDecimal spentUsd;

    /**
     * 货币代码
     */
    @TableField("currency_code")
    private String currencyCode;

    /**
     * 钱包记录 ID
     */
    @TableField("spent_wallet_record_id")
    private Long spentWalletRecordId;

    /**
     * 照片 URL 列表
     */
    @TableField("photo_urls")
    private String photoUrls;

    /**
     * 评价内容
     */
    @TableField("content")
    private String content;

    /**
     * 奖励金额（$PIC）
     */
    @TableField("earned")
    private BigDecimal earned;

    /**
     * 钱包（$PIC）记录 ID
     */
    @TableField("earned_wallet_record_id")
    private Long earnedWalletRecordId;

    /**
     * 点赞数
     */
    @TableField("likes_num")
    private Long likesNum;

    /**
     * 数据来源枚举：APP=应用创建；ADMIN=后台创建；UNKNOWN=未知
     */
    @TableField("`source`")
    private Source source;

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

    public static final String EARNED = "earned";
    public static final String SPENT_USD = "spent_usd";
    public static final String RESTAURANT_ID = "restaurant_id";
    public static final String RATE = "rate";


}
