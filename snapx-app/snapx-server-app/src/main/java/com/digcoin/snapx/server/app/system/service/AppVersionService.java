package com.digcoin.snapx.server.app.system.service;

import com.digcoin.snapx.domain.system.bo.VersionNumber;
import com.digcoin.snapx.domain.system.constant.Platform;
import com.digcoin.snapx.domain.system.entity.Version;
import com.digcoin.snapx.domain.system.service.VersionService;
import com.digcoin.snapx.server.app.system.converter.AppVersionConverter;
import com.digcoin.snapx.server.app.system.vo.AppVersionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/14 11:36
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppVersionService {

    private final VersionService versionService;

    private final AppVersionConverter appVersionConverter;

    public AppVersionVO getLatestVersion(Platform platform, String currentVersionNumber) {
        Version latestVersion = versionService.getLatestVersion(platform);
        // 如果没有发布记录，则不用发布
        if (Objects.isNull(latestVersion)) {
            return null;
        }
        VersionNumber latestVerNumber = VersionNumber.of(latestVersion.getVersionNumber());
        VersionNumber currentVerNumber = VersionNumber.of(currentVersionNumber);
        if (latestVerNumber.isGreaterThan(currentVerNumber)) {
            return appVersionConverter.intoVO(latestVersion);
        }
        return null;
    }
}
