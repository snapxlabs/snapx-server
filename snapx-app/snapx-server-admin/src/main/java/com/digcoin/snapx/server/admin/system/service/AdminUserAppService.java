package com.digcoin.snapx.server.admin.system.service;

import com.digcoin.snapx.core.common.util.PasswordUtil;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.system.bo.AdminUserQuery;
import com.digcoin.snapx.domain.system.entity.AdminUser;
import com.digcoin.snapx.domain.system.error.SystemDomainError;
import com.digcoin.snapx.domain.system.service.AdminUserService;
import com.digcoin.snapx.server.admin.system.converter.AdminUserConverter;
import com.digcoin.snapx.server.admin.system.dto.AdminUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserAppService {

    private final AdminUserService adminUserService;
    private final AdminUserConverter adminUserConverter;

    public AdminUserDTO createAdminUser(AdminUserDTO adminUser) {
        AdminUser entity = adminUserConverter.fromDTO(adminUser);
        if (Objects.isNull(adminUser.getPasswordInput())
                || !Arrays.equals(adminUser.getPasswordInput(), adminUser.getPasswordConfirm())) {
            throw SystemDomainError.ADMIN_REGISTER_PASSWORD_ERROR.withDefaults();
        }
        entity.setPassword(PasswordUtil.getEncryption(adminUser.getPasswordInput()));
        Long id = adminUserService.createAdminUser(entity);
        return adminUserConverter.intoDTO(adminUserService.findAdminUser(id));
    }

    public AdminUserDTO deleteAdminUser(Long id) {
        AdminUser adminUser = adminUserService.findAdminUser(id);
        adminUserService.deleteAdminUser(id);
        return adminUserConverter.intoDTO(adminUser);
    }

    public AdminUserDTO updateAdminUser(Long id, AdminUserDTO adminUser) {
        AdminUser entity = adminUserConverter.fromDTO(adminUser);
        entity.setId(id);
        if (Objects.nonNull(adminUser.getPasswordInput())) {
            if (!Arrays.equals(adminUser.getPasswordInput(), adminUser.getPasswordConfirm())) {
                throw SystemDomainError.ADMIN_REGISTER_PASSWORD_ERROR.withDefaults();
            }
            entity.setPassword(PasswordUtil.getEncryption(adminUser.getPasswordInput()));
        }
        adminUserService.updateAdminUser(entity);
        return adminUserConverter.intoDTO(adminUserService.findAdminUser(adminUser.getId()));
    }

    public AdminUserDTO findAdminUser(Long id) {
        return adminUserConverter.intoDTO(adminUserService.findAdminUser(id));
    }

    public PageResult<AdminUserDTO> pageAdminUser(AdminUserQuery query) {
        return PageResult.fromPageResult(adminUserService.pageAdminUser(query), adminUserConverter::intoDTO);
    }

    public void initAdmin() {
        // TODO: 加锁
        Long adminUserCount = adminUserService.countAdminUser(new AdminUserQuery());
        if (adminUserCount > 0L) {
            return;
        }

        AdminUserDTO user = new AdminUserDTO();
        String name = "系统管理员";
        String username = "admin";
        String password = RandomStringUtils.randomNumeric(6);
        user.setName(name);
        user.setUsername(username);
        user.setPasswordInput(password.getBytes(Charset.defaultCharset()));
        user.setPasswordConfirm(password.getBytes(Charset.defaultCharset()));
        AdminUserDTO userDTO = createAdminUser(user);
        log.info("admin init info: id:[{}] name:[{}] username:[{}] password:[{}]", userDTO.getId(), name, username, password);
    }

}
