package com.digcoin.snapx.server.base.infra.dto;

import com.digcoin.snapx.server.base.infra.component.CaptchaChannel;
import com.digcoin.snapx.server.base.infra.component.CaptchaType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CaptchaDTO {

    @Schema(description = "验证码发送渠道")
    private CaptchaChannel captchaChannel;

    @Schema(description = "验证码业务类型")
    private CaptchaType captchaType;

    @Schema(description = "验证码接收者")
    private String receiver;

    @Schema(description = "验证码失效时间", type = "string")
    private LocalDateTime expiredAt;

}
