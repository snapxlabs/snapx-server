package com.digcoin.snapx.server.app.member.dto;

import com.digcoin.snapx.core.common.enums.Gender;
import com.digcoin.snapx.core.json.deserializer.ByteArrayDeserializer;
import com.digcoin.snapx.server.base.infra.component.CaptchaChannel;
import com.digcoin.snapx.server.base.member.dto.BaseGiftCountUnitDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MemberDTO extends BaseGiftCountUnitDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "主键", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "account is blank")
    @Length(max = 255, message = "account to long")
    @Schema(description = "账号", type = "string", format = "email")
    private String account;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "手机号区域代码", type = "string", accessMode = Schema.AccessMode.READ_ONLY)
    private String phoneAreaCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "手机号", type = "string", accessMode = Schema.AccessMode.READ_ONLY)
    private String phone;

    @NotBlank(message = "nickname is blank")
    @Length(max = 32, message = "account to long")
    @Schema(description = "昵称")
    private String nickname;

    @Length(max = 255, message = "avatar url to long")
    @Schema(description = "头像图片地址")
    private String avatar;

    @Length(max = 255, message = "avatar cover url to long")
    @Schema(description = "主页背景图地址")
    private String avatarCover;

    @Schema(description = "性别枚举：MALE 男性；FEMALE 女性；UNKNOWN 未知；")
    private Gender gender;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "总共已赚" , accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalEarned;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "总共已赚usdc" , accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalUsdcCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "总共已赚积分" , accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalPointCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "账户是否冻结：0 否； 1是；", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean freeze;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "账号冻结原因", accessMode = Schema.AccessMode.READ_ONLY)
    private String freezeReason;

    @JsonDeserialize(using = ByteArrayDeserializer.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码", type = "string", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private byte[] passwordInput;

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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "创建时间", type = "string", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "更新时间", type = "string", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "邀请码", type = "string", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Long inviteId;

}
