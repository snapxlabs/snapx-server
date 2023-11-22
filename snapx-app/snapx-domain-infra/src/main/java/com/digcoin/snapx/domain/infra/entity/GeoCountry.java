package com.digcoin.snapx.domain.infra.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 地理 - 国家
 * </p>
 *
 * @author Generator
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
@TableName("inf_geo_country")
public class GeoCountry implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private Long id;

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
     * 货币代码
     */
    @TableField("currency_code")
    private String currencyCode;

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
