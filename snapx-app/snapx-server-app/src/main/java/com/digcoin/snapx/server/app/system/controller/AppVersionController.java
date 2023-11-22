package com.digcoin.snapx.server.app.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.domain.system.constant.Platform;
import com.digcoin.snapx.server.app.system.service.AppVersionService;
import com.digcoin.snapx.server.app.system.vo.AppVersionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/14 1:16
 * @description
 */
@Tag(name = "802 - APP版本更新")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/version")
@RequiredArgsConstructor
public class AppVersionController {

    private final AppVersionService appVersionService;

    @Operation(summary = "获取对应平台的最新可更新版本")
    @GetMapping("/get-last")
    public AppVersionVO getLatestVersion(@Schema(description = "平台, ANDROID-安卓 IOS-苹果", required = true)
                                      @RequestParam("platform") Platform platform,
                                         @Schema(description = "当前版本，格式：点号分隔的整数，例：1.0.0", required = true)
                                      @RequestParam("currentVersionNumber") String currentVersionNumber) {
        return appVersionService.getLatestVersion(platform, currentVersionNumber);
    }


}
