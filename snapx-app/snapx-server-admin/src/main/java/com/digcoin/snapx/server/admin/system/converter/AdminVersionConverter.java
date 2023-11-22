package com.digcoin.snapx.server.admin.system.converter;

import com.digcoin.snapx.domain.system.bo.VersionBO;
import com.digcoin.snapx.domain.system.entity.Version;
import com.digcoin.snapx.server.admin.system.dto.AdminVersionDTO;
import com.digcoin.snapx.server.admin.system.dto.AdminVersionQueryDTO;
import com.digcoin.snapx.server.admin.system.vo.AdminVersionVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/14 1:11
 * @description
 */
@Mapper(componentModel = "spring")
public interface AdminVersionConverter {

    Version fromDTO(AdminVersionDTO dto);

    VersionBO intoBO(AdminVersionQueryDTO dto);

    AdminVersionVO intoVO(Version version);

}
