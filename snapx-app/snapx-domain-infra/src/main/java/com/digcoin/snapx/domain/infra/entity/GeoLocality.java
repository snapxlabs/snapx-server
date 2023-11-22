package com.digcoin.snapx.domain.infra.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 地理 - 地点
 * </p>
 *
 * @author Generator
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
@TableName(value = "inf_geo_locality", autoResultMap = true)
public class GeoLocality implements Serializable {

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
     * 完整名称
     */
    @TableField("long_name")
    private String longName;

    /**
     * 缩写名称
     */
    @TableField("short_name")
    private String shortName;

    @TableField("long_name_zh_hk")
    private String longNameZhHk;

    @TableField("short_name_zh_hk")
    private String shortNameZhHk;

    /**
     * 关键词
     */
    @TableField(value = "keywords", typeHandler = JacksonTypeHandler.class)
    private List<String> keywords;

    /**
     * 是否推荐
     */
    @TableField("is_rec")
    private Boolean rec;

    /**
     * 排序
     */
    @TableField("weigh")
    private Long weigh;

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
