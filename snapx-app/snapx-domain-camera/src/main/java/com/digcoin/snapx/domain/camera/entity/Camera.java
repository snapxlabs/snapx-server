package com.digcoin.snapx.domain.camera.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 10:37
 * @description
 */
@Data
@TableName("cam_camera")
public class Camera {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 相机名称
     */
    private String name;

    /**
     * 相机编号
     */
    private String code;

    /**
     * 相机图片地址
     */
    private String pictureUrl;

    /**
     * 幸运
     */
    private Long luck;

    /**
     * 效能
     */
    private Long efficiency;

    /**
     * 恢复
     */
    private Long resilience;

    /**
     * 稳定
     */
    private Long stability;

    /**
     * 电池
     */
    private Long battery;

    /**
     * 内存
     */
    private Long memory;

    /**
     * 铸造
     */
    private Long mint;

    /**
     * 保养
     */
    private Long maintain;

    /**
     * 相机描述介绍
     */
    private String instruction;

    /**
     * 是否注册赠送
     */
    private Boolean isGift;

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
    private Long deleted;

}
