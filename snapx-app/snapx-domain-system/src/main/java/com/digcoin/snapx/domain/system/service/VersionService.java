package com.digcoin.snapx.domain.system.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.system.bo.VersionBO;
import com.digcoin.snapx.domain.system.constant.Platform;
import com.digcoin.snapx.domain.system.entity.Version;
import com.digcoin.snapx.domain.system.mapper.VersionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/13 16:15
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VersionService {

    private final VersionMapper versionMapper;

    public Version getVersion(Platform platform, String versionNumber) {
        return versionMapper.selectOne(Wrappers.lambdaQuery(Version.class)
                .eq(Version::getPlatform, platform)
                .eq(Version::getVersionNumber, versionNumber));
    }

    public Version getLatestVersion(Platform platform) {
        return versionMapper.selectOne(Wrappers.lambdaQuery(Version.class)
                .eq(Version::getPlatform, platform)
                .orderByDesc(Version::getVersionNumberInt).last("limit 0,1"));
    }

    public void createVersion(Version entity) {
        versionMapper.insert(entity);
    }

    public PageResult<Version> pageVersion(VersionBO versionBO) {
        return PageResult.fromPage(versionMapper.selectPage(PageHelper.getPage(versionBO),
                Wrappers.<Version>lambdaQuery()
                        .eq(Objects.nonNull(versionBO.getPlatform()), Version::getPlatform, versionBO.getPlatform())
                        .orderByDesc(Version::getCreateTime)), Function.identity());
    }

    public Version findById(Long id) {
        return versionMapper.selectById(id);
    }

    public void deleteVersion(Version version) {
        versionMapper.deleteById(version);
    }

    public void updateVersion(Version version) {
        versionMapper.updateById(version);
    }
}
