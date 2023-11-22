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
 * 餐厅
 * </p>
 *
 * @author Generator
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
@TableName("rst_restaurant")
public class Restaurant implements Serializable {

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
     * 餐厅名称
     */
    @TableField("name")
    private String name;

    /**
     * 餐厅名称（香港繁体）
     */
    @TableField("name_zh_hk")
    private String nameZhHk;

    /**
     * 餐厅封面 URL
     */
    @TableField("cover_url")
    private String coverUrl;

    /**
     * 平均赚取
     */
    @TableField("avg_earned")
    private String avgEarned;

    /**
     * 平均消费（美元）
     */
    @TableField("avg_spent_usd")
    private BigDecimal avgSpentUsd;

    /**
     * 平均评分：1-5
     */
    @TableField("avg_rating")
    private BigDecimal avgRating;

    /**
     * 评分1总数
     */
    @TableField("rating1_num")
    private Long rating1Num;

    /**
     * 评分2总数
     */
    @TableField("rating2_num")
    private Long rating2Num;

    /**
     * 评分3总数
     */
    @TableField("rating3_num")
    private Long rating3Num;

    /**
     * 评分4总数
     */
    @TableField("rating4_num")
    private Long rating4Num;

    /**
     * 评分5总数
     */
    @TableField("rating5_num")
    private Long rating5Num;

    /**
     * 谷歌地方 ID
     */
    @TableField("place_id")
    private String placeId;

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
     * 评价人数
     */
    @TableField("review_num")
    private Long reviewNum;

    /**
     * 访问量
     */
    @TableField("views")
    private Long views;

    /**
     * 是否已验证
     */
    @TableField("is_verified")
    private Boolean verified;

    /**
     * 是否谷歌餐厅
     */
    @TableField("is_google")
    private Boolean google;

    /**
     * 谷歌餐厅图片引用
     */
    @TableField("photo_reference")
    private String photoReference;

    /**
     * 图片宽度
     */
    @TableField("photo_width")
    private Integer photoWidth;

    /**
     * 图片高度
     */
    @TableField("photo_height")
    private Integer photoHeight;

    /**
     * 指定餐厅
     */
    @TableField("is_spec")
    private Boolean spec;

    /**
     * 是否开启水印
     */
    @TableField("is_watermark")
    private Boolean watermark;

    /**
     * 水印背景 URL
     */
    @TableField("watermark_bg_url")
    private String watermarkBgUrl;

    /**
     * 水印图标 URL
     */
    @TableField("watermark_logo_url")
    private String watermarkLogoUrl;

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


}
