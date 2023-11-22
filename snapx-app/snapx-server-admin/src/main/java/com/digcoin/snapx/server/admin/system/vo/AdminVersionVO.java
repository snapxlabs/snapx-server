package com.digcoin.snapx.server.admin.system.vo;

import com.digcoin.snapx.domain.system.constant.Platform;
import com.digcoin.snapx.domain.system.constant.VersionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/14 16:03
 * @description
 */
@Data
public class AdminVersionVO {

    @Schema(description = "版本Id")
    private Long id;

    @Schema(description = "平台, ANDROID-安卓 IOS-苹果")
    private Platform platform;

    @Schema(description = "版本号, 格式：点号分隔的整数，例：1.0.0")
    private String versionNumber;

    @Schema(description = "版本更新类型，FORCE-强制更新 PROMPT-非强制提示更新 NORMAL-非强制不提示更新")
    private VersionType versionType;

    @Schema(description = "版本说明")
    private String versionDescription;

    @Schema(description = "推送时间，空代表立刻推送")
    private LocalDateTime onlineTime;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

    @Schema(description = "修改时间", type = "string")
    private LocalDateTime updateTime;



}
