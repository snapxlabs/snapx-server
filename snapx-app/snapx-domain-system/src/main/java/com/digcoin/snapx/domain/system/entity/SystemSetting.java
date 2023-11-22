package com.digcoin.snapx.domain.system.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 9:34
 * @description
 */
@Data
public class SystemSetting implements Serializable {

    /**
     * 是否注册赠送
     */
    private Boolean isRegisterGift;

    /**
     * 注册赠送获得数量
     */
    private Long registerGiftCount;

    /**
     * 是否关注赠送
     */
    private Boolean isFollowGift;

    /**
     * 关注赠送获得数量
     */
    private Long followGiftCount;

    /**
     * 是否邀请赠送
     */
    private Boolean isInviteGift;

    /**
     * 邀请者获得数量
     */
    private Long inviteGiftCount;

    /**
     * 被邀请者获得数量
     */
    private Long beInvitedGiftCount;

    /**
     * 是否必须输入邀请码才能注册
     */
    private Boolean inviteCodeRequired;

    /**
     * 被邀请人获得积分时，邀请人的提成比例
     */
    private String pointsSharingRatio;

    /**
     * 被邀请人获得usdc时，邀请人的提成比例
     */
    private String usdcSharingRatio;

    /**
     * 赠送的积分数量（普通活动餐厅都生效）
     */
    private Double giftPointsCount;

    /**
     * 赠送的usdc最小数量（普通活动餐厅都生效）
     */
    private Double giftUsdcMinCount;

    /**
     * 赠送的usdc最大数量（普通活动餐厅都生效）
     */
    private Double giftUsdcMaxCount;

    /**
     * 赠送的积分数量（精选活动餐厅都生效）
     */
    private Double specGiftPointsCount;

    /**
     * 赠送的usdc最小数量（精选活动餐厅都生效）
     */
    private Double specGiftUsdcMinCount;

    /**
     * 赠送的usdc最大数量（精选活动餐厅都生效）
     */
    private Double specGiftUsdcMaxCount;

    /**
     * 是否开启精选额外赠送usdc
     */
    private Boolean isSpecExtraGift;

    /**
     * 是精选额外赠送usdc数量
     */
    private Double specExtraGiftUsdcMinCount;

    /**
     * 是精选额外赠送usdc数量
     */
    private Double specExtraGiftUsdcMaxCount;

}
