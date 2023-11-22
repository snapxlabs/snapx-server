package com.digcoin.snapx.domain.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/1/17 17:35
 * @description 社区活动
 */
@Data
@TableName("sys_activity")
public class Activity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动二维码base64地址
     */
    private String qrCodeBase64;

    /**
     * 活动开始时间
     */
    private LocalDate startDate;

    /**
     * 活动结束时间
     */
    private LocalDate endDate;

    /**
     * 活动介绍
     */
    private String introduction;

    /**
     * 活动位置
     */
    private String position;

    /**
     * 是否精选活动
     */
    private Boolean isSpec;

    /**
     * 活动是否启用
     */
    private Boolean isEnabled;

    /**
     * 排序，越大越靠前
     */
    private Integer sort;

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
    @TableLogic
    private Long deleted;
}
