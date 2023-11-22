package com.digcoin.snapx.domain.system.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;

@ParameterObject
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminUserQuery extends Pageable {

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机")
    private String phone;

    @Schema(description = "电子邮件")
    private String mail;

    @Schema(description = "停用/启用；false 停用；true 启用", type = "boolean")
    private Boolean enabled;

}
