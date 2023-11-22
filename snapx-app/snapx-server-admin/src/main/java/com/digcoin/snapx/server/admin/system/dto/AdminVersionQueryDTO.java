package com.digcoin.snapx.server.admin.system.dto;

import com.digcoin.snapx.core.mybatis.Pageable;
import com.digcoin.snapx.domain.system.constant.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/14 16:07
 * @description
 */
@Data
public class AdminVersionQueryDTO extends Pageable {

    @Schema(description = "平台, ANDROID-安卓 IOS-苹果", allowableValues = "ANDROID,IOS")
    private Platform platform;

}
