package com.digcoin.snapx.server.admin.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class MemberFreezeDTO {

    @NotNull(message = "freeze can not be null")
    @Schema(description = "账户是否冻结：0 否； 1是；")
    private Boolean freeze;

    @Length(max = 255, message = "freeze reason length over 255")
    @Schema(description = "账号冻结原因")
    private String freezeReason;

}
