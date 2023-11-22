package com.digcoin.snapx.server.app.member.dto;

import com.digcoin.snapx.server.base.infra.component.CaptchaChannel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MemberPhoneDTO {

    @Schema(description = "主键", hidden = true)
    private Long memberId;

    @NotBlank(message = "area code is blank")
    @Schema(description = "手机号区域代码")
    private String areaCode;

    @NotBlank(message = "phone is blank")
    @Schema(description = "手机号")
    private String phone;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true, description = "验证码发送渠道枚举：EMAIL，SMS，CONSOLE", accessMode = Schema.AccessMode.WRITE_ONLY)
    private CaptchaChannel captchaChannel;

    @NotBlank(message = "verification code is blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "验证码", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String captcha;

}
