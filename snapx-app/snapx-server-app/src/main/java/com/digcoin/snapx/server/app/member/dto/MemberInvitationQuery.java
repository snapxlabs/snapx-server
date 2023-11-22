package com.digcoin.snapx.server.app.member.dto;

import com.digcoin.snapx.core.mybatis.Pageable;
import com.digcoin.snapx.domain.member.bo.MemberQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;

@Data
@ParameterObject
public class MemberInvitationQuery extends Pageable {

    @Schema(hidden = true)
    private Long inviterMemberId;

    public MemberQuery toMemberQuery() {
        MemberQuery memberQuery = new MemberQuery();
        BeanUtils.copyProperties(this, memberQuery);
        return memberQuery;
    }

}
