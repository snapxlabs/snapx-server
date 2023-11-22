package com.digcoin.snapx.domain.member.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mem_member_discord_binding")
public class MemberDiscordBinding {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * discord用户id
     */
    private String discordUserId;

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

}
