package com.digcoin.snapx.domain.member.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("mem_inviter_commission")
public class InviterCommission {

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
     * 被邀请人收入金额
     */
    private Long inviteeIncomeAmount;

    /**
     * 邀请人分佣金额
     */
    private Long inviterCommissionAmount;

    /**
     * 账户类型
     */
    private String accountType;

    /**
     * 分佣比例
     */
    private String sharingRatio;

    /**
     * 邀请人账户明细id
     */
    private Long inviterAccountDetailsId;

    /**
     * 被邀请人账户明细id
     */
    private Long inviteeAccountDetailsId;

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
