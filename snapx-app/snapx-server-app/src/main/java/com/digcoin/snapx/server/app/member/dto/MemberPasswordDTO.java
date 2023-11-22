package com.digcoin.snapx.server.app.member.dto;

import com.digcoin.snapx.core.json.deserializer.ByteArrayDeserializer;
import com.digcoin.snapx.server.base.infra.component.CaptchaChannel;
import com.digcoin.snapx.server.base.infra.component.CaptchaType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class MemberPasswordDTO {

    @Schema(description = "主键", hidden = true)
    private Long memberId;

    @Schema(description = "账号，忘记密码时需要传该参数", type = "string", format = "email")
    private String account;

    @NotEmpty(message = "password is blank")
    @JsonDeserialize(using = ByteArrayDeserializer.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码", type = "string", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private byte[] passwordInput;

    @NotEmpty(message = "password confirm is blank")
    @JsonDeserialize(using = ByteArrayDeserializer.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "重复密码", type = "string", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private byte[] passwordConfirm;

    @NotBlank(message = "verification code is blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "验证码", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String captcha;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(hidden = true, description = "验证码发送渠道枚举：EMAIL，SMS，CONSOLE", accessMode = Schema.AccessMode.WRITE_ONLY)
    private CaptchaChannel captchaChannel;

    @JsonIgnore
    @Schema(hidden = true)
    private CaptchaType captchaType;

}
