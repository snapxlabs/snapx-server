package com.digcoin.snapx.server.base.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collection;

@Data
public class MemberAvatarDeleteDTO {

    @Schema(description = "头像id集合")
    private Collection<Long> ids;

}
