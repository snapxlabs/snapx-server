package com.digcoin.snapx.domain.member.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/23 10:44
 * @description
 */
@Data
@TableName("mem_member_interaction")
public class MemberInteraction {

    /**
     * 主键i
     */
    @TableId
    private Long id;

    /**
     * 会员Id
     */
    private Long memberId;

    /**
     * 是否关注Twitter
     */
    private Boolean isFollowTwitter;

    /**
     * 关注时间
     */
    private LocalDateTime followTwitterTime;

    /**
     * 是否加入社区
     */
    private Boolean isJoinDiscord;

    /**
     * 加入时间
     */
    private LocalDateTime joinDiscordTime;

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
