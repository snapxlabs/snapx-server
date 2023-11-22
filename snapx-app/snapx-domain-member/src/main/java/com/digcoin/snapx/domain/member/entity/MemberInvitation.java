package com.digcoin.snapx.domain.member.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digcoin.snapx.domain.member.constant.MemberIdentity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员邀新记录
 */
@Data
@TableName("mem_member_invitation")
public class MemberInvitation {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 邀请人会员id
     */
    private Long inviterMemberId;

    /**
     * 被邀请人会员id
     */
    private Long inviteeMemberId;

    /**
     * 邀请人奖励胶卷数 inviter_gift_count
     */
    private Long inviterGiftCount;

    /**
     * 被邀请人奖励胶卷数 invitee_gift_count
     */
    private Long inviteeGiftCount;

    /**
     * 邀请人会员身份：GENERAL 普通会员；AGENT 代理人
     */
    private MemberIdentity identity;

    /**
     * 被邀请人获得积分时，邀请人的提成比例
     */
    private String pointsSharingRatio;

    /**
     * 被邀请人获得usdc时，邀请人的提成比例
     */
    private String usdcSharingRatio;

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
