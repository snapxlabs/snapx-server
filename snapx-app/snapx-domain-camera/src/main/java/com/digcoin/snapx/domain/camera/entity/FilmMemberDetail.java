package com.digcoin.snapx.domain.camera.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digcoin.snapx.domain.camera.enums.FilmChangeType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/23 10:41
 * @description
 */
@Data
@TableName("cam_film_member_detail")
public class FilmMemberDetail {

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
     * 来源类型
     */
    private FilmChangeType filmChangeType;

    /**
     * 变动数量 正数表示增加 负数表示减少
     */
    private Long variableQuantity;

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
