package com.digcoin.snapx.server.admin.system.converter;

import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.system.entity.AdminUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrentUserConverter {

    CurrentUser fromAdminUser(AdminUser adminUser);

}
