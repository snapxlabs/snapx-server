package com.digcoin.snapx.server.app.member.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 11:25
 * @description
 */
@Data
public class AppMemberInteractionVO {

    @Schema(description = "会员Id")
    private Long memberId;

    @Schema(description = "是否关注Twitter")
    private Boolean isFollowTwitter;

    @Schema(description = "关注时间", type = "string")
    private LocalDateTime followTwitterTime;

    @Schema(description = "是否加入社区")
    private Boolean isJoinDiscord;

    @Schema(description = "关注赠送获得数量")
    private Long followGiftCount;

    @Schema(description = "加入时间", type = "string")
    private LocalDateTime joinDiscordTime;

    @Schema(description = "Discord授权链接")
    private String discordUrl;

    @Schema(description = "Twitter授权链接")
    private String twitterUrl;
}
