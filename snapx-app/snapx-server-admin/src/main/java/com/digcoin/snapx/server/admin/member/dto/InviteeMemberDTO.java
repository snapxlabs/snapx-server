package com.digcoin.snapx.server.admin.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class InviteeMemberDTO {

    @Schema(description = "会员信息")
    private MemberDTO member;

    @Schema(description = "邀请人奖励胶卷数")
    private Long inviterGiftCount;

    @Schema(description = "被邀请人奖励胶卷数")
    private Long inviteeGiftCount;

}
