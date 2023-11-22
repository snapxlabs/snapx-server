package com.digcoin.snapx.server.admin.camera.converter;

import com.digcoin.snapx.domain.camera.bo.CameraMemberQueryBO;
import com.digcoin.snapx.domain.camera.entity.CameraMember;
import com.digcoin.snapx.server.admin.camera.dto.AdminCameraMemberPageDTO;
import com.digcoin.snapx.server.admin.camera.vo.AdminCameraMemberVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/23 0:27
 * @description
 */
@Mapper(componentModel = "spring")
public interface AdminCameraMemberConverter {

    CameraMemberQueryBO convertBO(AdminCameraMemberPageDTO dto);

    AdminCameraMemberVO intoVO(CameraMember cameraMember);
}
