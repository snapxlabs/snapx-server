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
 * @date 2023/2/23 9:55
 * @description
 */
@Data
@TableName("cam_film_member")
public class FilmMember {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 会员Id
     */
    private Long memberId;

    /**
     * 可用数量
     */
    private Long remainingQuantity;

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

}
