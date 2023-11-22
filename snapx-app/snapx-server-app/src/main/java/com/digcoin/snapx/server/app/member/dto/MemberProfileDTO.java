package com.digcoin.snapx.server.app.member.dto;

import com.digcoin.snapx.core.common.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MemberProfileDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "主键", hidden = true)
    private Long memberId;

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

}
