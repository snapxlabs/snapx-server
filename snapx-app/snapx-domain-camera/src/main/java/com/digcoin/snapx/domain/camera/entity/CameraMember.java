package com.digcoin.snapx.domain.camera.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digcoin.snapx.domain.camera.enums.CameraSource;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 15:03
 * @description
 */
@Data
@TableName("cam_camera_member")
public class CameraMember {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 相机Id
     */
    private Long cameraId;

    /**
     * 会员Id
     */
    private Long memberId;

    /**
     * 相机来源
     */
    private CameraSource cameraSource;

    /**
     * 当前相机经验
     */
    private Long experience;

    /**
     * 相机等级
     */
    private Long currentLevel;

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
    private String memberNickName;

    @TableField(exist = false)
    private String memberAccount;

    @TableField(exist = false)
    private String cameraName;

    @TableField(exist = false)
    private String cameraCode;

}
