package com.digcoin.snapx.server.admin.system.converter;


import com.digcoin.snapx.domain.system.bo.ActivityQueryBO;
import com.digcoin.snapx.domain.system.entity.Activity;
import com.digcoin.snapx.server.admin.system.dto.AdminActivityDTO;
import com.digcoin.snapx.server.admin.system.dto.AdminActivityQueryDTO;
import com.digcoin.snapx.server.admin.system.vo.AdminActivityVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/17 16:16
 * @description
 */
@Mapper(componentModel = "spring")
public interface AdminActivityConverter {

    Activity fromDTO(AdminActivityDTO dto);

    ActivityQueryBO intoBO(AdminActivityQueryDTO dto);

    AdminActivityVO intoVO(Activity activity);

}
