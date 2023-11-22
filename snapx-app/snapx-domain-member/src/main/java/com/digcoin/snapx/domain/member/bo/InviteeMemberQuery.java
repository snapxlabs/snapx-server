package com.digcoin.snapx.domain.member.bo;

import com.digcoin.snapx.core.common.enums.Gender;
import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@ParameterObject
public class InviteeMemberQuery extends Pageable {

    @Schema(description = "用户账号")
    private String account;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户性别。性别枚举：MALE 男性；FEMALE 女性；UNKNOWN 未知；")
    private Gender gender;

    @Schema(description = "用户创建时间（开始时间）", type = "string")
    private LocalDateTime createTimeStart;

    @Schema(description = "用户创建时间（结束时间）", type = "string")
    private LocalDateTime createTimeEnd;

    @Schema(description = "邀请人id")
    private Long inviterMemberId;

    public MemberQuery toMemberQuery() {
        MemberQuery memberQuery = new MemberQuery();
        BeanUtils.copyProperties(this, memberQuery);
        return memberQuery;
    }

}
