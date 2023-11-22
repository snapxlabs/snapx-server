package com.digcoin.snapx.server.base.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 16:31
 * @description
 */
@Data
public class AdminSystemSettingVO {

    @Schema(description = "是否注册赠送")
    private Boolean isRegisterGift;

    @Schema(description = "注册赠送获得数量")
    private Long registerGiftCount;

    @Schema(description = "是否关注赠送获得数量")
    private Boolean isFollowGift;

    @Schema(description = "关注赠送获得数量")
    private Long followGiftCount;

    @Schema(description = "是否邀请赠送")
    private Boolean isInviteGift;

    @Schema(description = "邀请者获得数量")
    private Long inviteGiftCount;

    @Schema(description = "被邀请者获得数量")
    private Long beInvitedGiftCount;

    @Schema(description = "是否必须输入邀请码才能注册")
    private Boolean inviteCodeRequired;

    @Schema(description = "被邀请人获得积分时，邀请人的提成比例")
    private String pointsSharingRatio;

    @Schema(description = "被邀请人获得usdc时，邀请人的提成比例")
    private String usdcSharingRatio;

    @Schema(description = "赠送的积分数量（普通活动餐厅都生效）")
    private Double giftPointsCount;

    @Schema(description = "赠送的usdc最小数量（普通活动餐厅都生效）")
    private Double giftUsdcMinCount;

    @Schema(description = "赠送的usdc最大数量（普通活动餐厅都生效）")
    private Double giftUsdcMaxCount;

    @Schema(description = "赠送的积分数量（精选活动餐厅都生效）")
    private Double specGiftPointsCount;

    @Schema(description = "赠送的usdc最小数量（精选活动餐厅都生效）")
    private Double specGiftUsdcMinCount;

    @Schema(description = "赠送的usdc最大数量（精选活动餐厅都生效）")
    private Double specGiftUsdcMaxCount;

    @Schema(description = "是否开启精选额外赠送usdc")
    private Boolean isSpecExtraGift;

    @Schema(description = "是精选额外赠送usdc数量")
    private Double specExtraGiftUsdcMinCount;

    @Schema(description = "是精选额外赠送usdc数量")
    private Double specExtraGiftUsdcMaxCount;



}
