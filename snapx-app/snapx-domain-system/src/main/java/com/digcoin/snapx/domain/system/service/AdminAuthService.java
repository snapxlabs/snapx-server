package com.digcoin.snapx.domain.system.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.util.PasswordUtil;
import com.digcoin.snapx.domain.system.entity.AdminUser;
import com.digcoin.snapx.domain.system.error.SystemDomainError;
import com.digcoin.snapx.domain.system.mapper.AdminUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminUserMapper adminUserMapper;

    public AdminUser loginByUsername(String username, byte[] password) {

        AdminUser adminUser = adminUserMapper.selectOne(Wrappers.lambdaQuery(AdminUser.class)
                .eq(AdminUser::getUsername, username));
        if (Objects.isNull(adminUser)) {
            throw SystemDomainError.ADMIN_LOGIN_USERNAME_ERROR.withDefaults();
        }

        if (!matchPassword(adminUser, password)) {
            throw SystemDomainError.ADMIN_LOGIN_PASSWORD_ERROR.withDefaults();
        }

        checkAdminUserStatus(adminUser);

        return adminUser;
    }

    protected boolean matchPassword(AdminUser adminUser, byte[] password) {
        return PasswordUtil.match(adminUser.getPassword(), password);
    }

    protected void checkAdminUserStatus(AdminUser adminUser) {
        if (!adminUser.getEnabled()) {
            throw SystemDomainError.ADMIN_LOGIN_DISABLE_ERROR.withDefaults();
        }
    }

}
