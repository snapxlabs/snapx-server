package com.digcoin.snapx.server.app.member.dto;

import com.digcoin.snapx.core.mybatis.PageResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberInvitationDTO {

    @Schema(description = "邀请人")
    private MemberDTO inviter;

    @Schema(description = "被邀请人列表")
    private PageResult<MemberDTO> invited;

}
