package com.digcoin.snapx.server.admin.member.dto;

import com.digcoin.snapx.core.common.enums.Gender;
import com.digcoin.snapx.domain.member.constant.MemberIdentity;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "主键", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像URL地址")
    private String avatar;

    @Schema(description = "用户主页背景图地址")
    private String avatarCover;

    @Schema(description = "性别枚举：MALE 男性；FEMALE 女性；UNKNOWN 未知；")
    private Gender gender;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "最后一次访问时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime lastAccessTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "邀请人数", accessMode = Schema.AccessMode.READ_ONLY)
    private Long inviteeCount;

    @Schema(description = "账户是否冻结：0 否； 1是；")
    private Boolean freeze;

    @Schema(description = "账号冻结原因")
    private String freezeReason;

    @Schema(description = "会员身份：GENERAL 普通会员；AGENT 代理人")
    private MemberIdentity identity;

    @Schema(description = "用户等级")
    private Integer level;

    @Schema(description = "代币钱包地址")
    private String wallets;

    @Schema(description = "该用户的邀请人")
    private MemberDTO inviterMember;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;

    @Schema(description = "手机号区域代码")
    private String phoneAreaCode;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "是否虚拟用户：0 否；1 是；")
    private Boolean virtualUser;

}
