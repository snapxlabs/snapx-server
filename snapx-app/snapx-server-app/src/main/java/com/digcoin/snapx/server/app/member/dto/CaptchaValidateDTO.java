package com.digcoin.snapx.server.app.member.dto;

import com.digcoin.snapx.server.base.infra.component.CaptchaChannel;
import com.digcoin.snapx.server.base.infra.component.CaptchaType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class CaptchaValidateDTO {

    @NotBlank(message = "account is blank")
    @Length(max = 255, message = "account to long")
    @Schema(description = "账号", type = "string", format = "email")
    private String account;

    @NotBlank(message = "verification code is blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "验证码", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String captcha;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true, description = "验证码发送渠道枚举：EMAIL，SMS，CONSOLE", accessMode = Schema.AccessMode.WRITE_ONLY)
    private CaptchaChannel captchaChannel;

    @Schema(hidden = true)
    private CaptchaType captchaType;
}
