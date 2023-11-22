package com.digcoin.snapx.server.admin.system.service;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.system.bo.VersionBO;
import com.digcoin.snapx.domain.system.bo.VersionNumber;
import com.digcoin.snapx.domain.system.constant.Platform;
import com.digcoin.snapx.domain.system.entity.Version;
import com.digcoin.snapx.domain.system.error.VersionError;
import com.digcoin.snapx.domain.system.service.VersionService;
import com.digcoin.snapx.server.admin.system.converter.AdminVersionConverter;
import com.digcoin.snapx.server.admin.system.dto.AdminVersionDTO;
import com.digcoin.snapx.server.admin.system.dto.AdminVersionQueryDTO;
import com.digcoin.snapx.server.admin.system.vo.AdminVersionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/13 20:09
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminVersionService {

    private final VersionService versionService;

    private final AdminVersionConverter adminVersionConverter;

    @Transactional(rollbackFor = Exception.class)
    public void createVersion(AdminVersionDTO dto) {
        Platform platform = dto.getPlatform();
        String versionNumber = dto.getVersionNumber();
        if(!this.matchVersion(versionNumber)){
            throw VersionError.VERSION_NUMBER_ERROR.withDefaults();
        }
        Version entity = versionService.getVersion(platform, versionNumber);
        if(Objects.nonNull(entity)){
            throw VersionError.VERSION_NUMBER_EXISTS.withDefaults();
        }
        Version latestVersion = versionService.getLatestVersion(platform);
        if(Objects.nonNull(latestVersion)){
            VersionNumber latestVerNumber = VersionNumber.of(latestVersion.getVersionNumber());
            VersionNumber futureVerNumber = VersionNumber.of(versionNumber);
            if (latestVerNumber.isGreaterThan(futureVerNumber)) {
                throw VersionError.VERSION_NUMBER_NOT_LATEST.withDefaults();
            }
        }
        entity =  adminVersionConverter.fromDTO(dto);
        Integer versionInt = Optional.ofNullable(latestVersion).map(Version::getVersionNumberInt).map(x -> x + 1).orElse(1);
        entity.setVersionNumberInt(versionInt);
        versionService.createVersion(entity);
    }

    public PageResult<AdminVersionVO> pageVersion(AdminVersionQueryDTO dto) {
        VersionBO versionBO = adminVersionConverter.intoBO(dto);
        PageResult<Version> versionPageResult = versionService.pageVersion(versionBO);
        return PageResult.fromPageResult(versionPageResult, adminVersionConverter::intoVO);
    }


    public AdminVersionVO getVersion(Long id) {
        return Optional.ofNullable(versionService.findById(id)).map(
                adminVersionConverter::intoVO).orElseThrow(VersionError.VERSION_NOT_EXISTS::withDefaults);
    }

    public void deleteVersion(Long id) {
        Optional<Version> optional = Optional.ofNullable(versionService.findById(id));
        if (optional.isEmpty()) {
            throw VersionError.VERSION_NOT_EXISTS.withDefaults();
        }
        versionService.deleteVersion(optional.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public void editVersion(Long id, AdminVersionDTO dto) {
        Optional<Version> optional = Optional.ofNullable(versionService.findById(id));
        if (optional.isEmpty()) {
            throw VersionError.VERSION_NOT_EXISTS.withDefaults();
        }
        Version version = optional.get();
        if(!dto.getVersionNumber().equals(version.getVersionNumber())){
            Platform platform = dto.getPlatform();
            String versionNumber = dto.getVersionNumber();
            if(!this.matchVersion(versionNumber)){
                throw VersionError.VERSION_NUMBER_ERROR.withDefaults();
            }

            Version existVersion = versionService.getVersion(platform, versionNumber);
            if(Objects.nonNull(existVersion)){
                throw VersionError.VERSION_NUMBER_EXISTS.withDefaults();
            }

            Version latestVersion = versionService.getLatestVersion(platform);
            if(Objects.nonNull(latestVersion)){
                VersionNumber latestVerNumber = VersionNumber.of(latestVersion.getVersionNumber());
                VersionNumber futureVerNumber = VersionNumber.of(versionNumber);
                if (latestVerNumber.isGreaterThan(futureVerNumber)) {
                    throw VersionError.VERSION_NUMBER_NOT_LATEST.withDefaults();
                }
            }
        }
        version = adminVersionConverter.fromDTO(dto);
        version.setId(id);
        versionService.updateVersion(version);
    }


    private boolean matchVersion(String version){
        return (Pattern.compile("^(\\d+)(.(\\d+)){2}$").matcher(version)).matches();
    }
}
