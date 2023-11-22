package com.digcoin.snapx.server.app.camera.converter;

import com.digcoin.snapx.domain.camera.entity.CameraMember;
import com.digcoin.snapx.server.app.camera.vo.AppCameraMemberVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 17:46
 * @description
 */
@Mapper(componentModel = "spring")
public interface AppCameraMemberConverter {

    AppCameraMemberVO intoVO (CameraMember cameraMember);


}
