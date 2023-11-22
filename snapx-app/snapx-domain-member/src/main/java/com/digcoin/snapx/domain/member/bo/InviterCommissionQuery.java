package com.digcoin.snapx.domain.member.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

@Data
@ParameterObject
public class InviterCommissionQuery extends Pageable {

    @Schema(description = "邀请人会员id")
    private Long inviterMemberId;

    @Schema(description = "账户类型")
    private String accountType;

}
