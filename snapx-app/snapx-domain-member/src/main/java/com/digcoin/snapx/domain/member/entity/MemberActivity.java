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
 * @date 2023/4/4 0:7
 * @description
 */
@Data
@TableName("mem_member_activity")
public class MemberActivity {

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
     * 活动Id
     */
    private Long activityId;

    /**
     * 参与活动时纬度
     */
    private String lat;

    /**
     * 参与活动时经度
     */
    private String lng;

//    /**
//     * 赠送的积分数量
//     */
//    private BigDecimal giftPointsCount;
//
//    /**
//     * 赠送的usdc数量
//     */
//    private BigDecimal giftUsdcCount;
//
//    /**
//     * 精选赠送的积分数量
//     */
//    private BigDecimal specGiftPointsCount;
//
//    /**
//     * 精选赠送的usdc数量
//     */
//    private BigDecimal specGiftUsdcCount;
//
//    /**
//     * 精选额外赠送的usdc数量
//     */
//    private BigDecimal specGiftExtraUsdcCount;
//
//    /**
//     * 赠送积分总数量
//     */
//    private BigDecimal totalGiftPointsCount;
//
//    /**
//     * 赠送usdc总数量
//     */
//    private BigDecimal totalGiftUsdcCount;

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
    private String memberAccount;

    @TableField(exist = false)
    private String activityName;

}
