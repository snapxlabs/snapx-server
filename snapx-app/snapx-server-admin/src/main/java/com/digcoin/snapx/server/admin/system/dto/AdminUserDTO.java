package com.digcoin.snapx.server.admin.system.dto;

import com.digcoin.snapx.core.json.deserializer.ByteArrayDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class AdminUserDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "主键", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "姓名不能为空")
    @Length(max = 16, message = "姓名过长")
    @Schema(description = "姓名")
    private String name;

    @NotBlank(message = "用户名不能为空")
    @Length(max = 32, message = "用户名过长")
    @Schema(description = "用户名")
    private String username;

    @JsonDeserialize(using = ByteArrayDeserializer.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "输入密码", type = "string", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private byte[] passwordInput;

    @JsonDeserialize(using = ByteArrayDeserializer.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "确认密码", type = "string", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private byte[] passwordConfirm;

    @Length(max = 32, message = "手机号码过长")
    @Schema(description = "手机号码")
    private String phone;

    @Email(message = "电子邮件格式不正确")
    @Length(max = 64, message = "电子邮件过长")
    @Schema(description = "电子邮件", type = "string", format = "email")
    private String mail;

    @Length(max = 255, message = "备注过长")
    @Schema(description = "备注")
    private String remark;

    @NotNull(message = "停用/启用标识不能为空")
    @Schema(description = "停用/启用标识；false 停用；true 启用")
    private Boolean enabled;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "创建时间", type = "string", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "更新时间", type = "string", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;

}
