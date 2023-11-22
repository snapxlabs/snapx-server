package com.digcoin.snapx.server.admin.system.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.system.entity.AdminUser;
import com.digcoin.snapx.domain.system.service.AdminAuthService;
import com.digcoin.snapx.domain.system.service.AdminUserService;
import com.digcoin.snapx.server.admin.system.converter.AdminUserConverter;
import com.digcoin.snapx.server.admin.system.converter.CurrentUserConverter;
import com.digcoin.snapx.server.admin.system.converter.TokenInfoConverter;
import com.digcoin.snapx.server.admin.system.dto.AdminUserAuthDTO;
import com.digcoin.snapx.server.admin.system.dto.AdminUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAuthAppService {

    private final AdminAuthService adminAuthService;
    private final AdminUserService adminUserService;
    private final AdminUserConverter adminUserConverter;
    private final TokenInfoConverter tokenInfoConverter;
    private final CurrentUserConverter currentUserConverter;

    public AdminUserAuthDTO login(String username, byte[] password) {
        AdminUser adminUser = adminAuthService.loginByUsername(username, password);
        CurrentUser currentUser = currentUserConverter.fromAdminUser(adminUser);
        StpUtil.login(adminUser.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        SaSession session = StpUtil.getSession();
        session.set(CurrentUser.IDENTITY, currentUser);

        AdminUserAuthDTO adminUserAuth = new AdminUserAuthDTO();
        adminUserAuth.setUserDetail(adminUserConverter.intoDTO(adminUser));
        adminUserAuth.setTokenInfo(tokenInfoConverter.intoDTO(tokenInfo));

        return adminUserAuth;
    }

    public AdminUserDTO findCurrentUserInfo(Long id) {
        AdminUser adminUser = adminUserService.findAdminUser(id);
        return adminUserConverter.intoDTO(adminUser);
    }

    public void logout() {
        StpUtil.logout();
    }

}
