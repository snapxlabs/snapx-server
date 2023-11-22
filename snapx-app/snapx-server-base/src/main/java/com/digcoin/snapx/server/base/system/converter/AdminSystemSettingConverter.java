package com.digcoin.snapx.server.base.system.converter;

import com.digcoin.snapx.domain.system.entity.SystemSetting;
import com.digcoin.snapx.server.base.system.dto.AdminSystemSettingDTO;
import com.digcoin.snapx.server.base.system.vo.AdminSystemSettingVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 16:08
 * @description
 */
@Mapper(componentModel = "spring")
public interface AdminSystemSettingConverter {

    SystemSetting fromDTO(AdminSystemSettingDTO dto);

    AdminSystemSettingVO intoVO(SystemSetting systemSetting);

}
