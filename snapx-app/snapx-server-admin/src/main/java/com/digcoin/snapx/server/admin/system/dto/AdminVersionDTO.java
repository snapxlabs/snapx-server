package com.digcoin.snapx.server.admin.system.dto;

import com.digcoin.snapx.domain.system.constant.Platform;
import com.digcoin.snapx.domain.system.constant.VersionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/13 20:08
 * @description
 */
@Data
public class AdminVersionDTO {

    @NotNull(message = "Platform cannot be null ")
    @Schema(description = "平台, ANDROID-安卓 IOS-苹果", allowableValues = "ANDROID,IOS", required = true)
    private Platform platform;

    @NotBlank(message = "Version number cannot be null")
    @Length(max = 10, message = "The maximum length of the version number cannot exceed 10 characters")
    @Schema(description = "版本号, 格式：点号分隔的整数，例：1.0.0", required = true)
    private String versionNumber;

    @NotNull(message = "Version type cannot be null")
    @Schema(description = "版本更新类型，FORCE-强制更新 PROMPT-非强制提示更新 NORMAL-非强制不提示更新",
            allowableValues ="FORCE,PROMPT,NORMAL", required = true)
    private VersionType versionType;

    @Length(max = 200, message = "The maximum length of the version description cannot exceed 500 characters")
    @Schema(description = "版本说明")
    private String versionDescription ;

    @Schema(description = "推送时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime onlineTime;

}
