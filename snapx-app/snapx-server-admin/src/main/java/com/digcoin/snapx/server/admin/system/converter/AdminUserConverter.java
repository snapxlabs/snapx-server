package com.digcoin.snapx.server.admin.system.converter;

import com.digcoin.snapx.domain.system.entity.AdminUser;
import com.digcoin.snapx.server.admin.system.dto.AdminUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminUserConverter {

    AdminUser fromDTO(AdminUserDTO adminUser);

    AdminUserDTO intoDTO(AdminUser adminUser);

}
