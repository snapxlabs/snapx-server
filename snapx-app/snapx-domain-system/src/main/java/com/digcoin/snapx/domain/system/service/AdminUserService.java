package com.digcoin.snapx.domain.system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.domain.system.bo.AdminUserQuery;
import com.digcoin.snapx.domain.system.entity.AdminUser;
import com.digcoin.snapx.domain.system.error.SystemDomainError;
import com.digcoin.snapx.domain.system.mapper.AdminUserMapper;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserMapper adminUserMapper;

    public Long createAdminUser(AdminUser adminUser) {
        adminUserMapper.insert(adminUser);
        return adminUser.getId();
    }

    public void deleteAdminUser(Long id) {
        adminUserMapper.deleteById(id);
    }

    public void updateAdminUser(AdminUser adminUser) {
        adminUserMapper.updateById(adminUser);
    }

    public AdminUser findAdminUser(Long id) {
        return adminUserMapper.selectById(id);
    }

    public PageResult<AdminUser> pageAdminUser(AdminUserQuery query) {
        return PageResult.fromPage(adminUserMapper.selectPage(
                PageHelper.getPage(query),
                buildWrapper(query)),
                Function.identity());
    }

    public List<AdminUser> listAdminUser(AdminUserQuery query) {
        return adminUserMapper.selectList(buildWrapper(query));
    }

    public Long countAdminUser(AdminUserQuery query) {
        return adminUserMapper.selectCount(buildWrapper(query));
    }

    private Wrapper<AdminUser> buildWrapper(AdminUserQuery query) {
        return Wrappers.lambdaUpdate(AdminUser.class)
                .like(StringUtils.isNotBlank(query.getName()), AdminUser::getName, query.getName())
                .like(StringUtils.isNotBlank(query.getUsername()), AdminUser::getUsername, query.getUsername())
                .like(StringUtils.isNotBlank(query.getPhone()), AdminUser::getPhone, query.getPhone())
                .like(StringUtils.isNotBlank(query.getMail()), AdminUser::getMail, query.getPhone())
                .eq(Objects.nonNull(query.getEnabled()), AdminUser::getEnabled, query.getEnabled())
                .orderByDesc(AdminUser::getCreateTime);
    }
}
