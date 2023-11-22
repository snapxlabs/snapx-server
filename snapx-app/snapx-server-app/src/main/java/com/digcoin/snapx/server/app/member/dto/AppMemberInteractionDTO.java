package com.digcoin.snapx.server.app.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 11:43
 * @description
 */
@Data
public class AppMemberInteractionDTO {

    @Schema(description = "是否关注Twitter")
    private Boolean isFollowTwitter;

    @Schema(description = "是否加入社区")
    private Boolean isJoinDiscord;

    @Schema(hidden = true)
    private Long memberId;
}
