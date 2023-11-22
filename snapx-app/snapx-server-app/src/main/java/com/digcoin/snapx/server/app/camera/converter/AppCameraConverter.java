package com.digcoin.snapx.server.app.camera.converter;

import com.digcoin.snapx.domain.camera.bo.CameraQueryBO;
import com.digcoin.snapx.domain.camera.entity.Camera;
import com.digcoin.snapx.server.app.camera.dto.AppCameraQueryDTO;
import com.digcoin.snapx.server.app.camera.vo.AppCameraVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 16:00
 * @description
 */
@Mapper(componentModel = "spring")
public interface AppCameraConverter {

    AppCameraVO intoVO(Camera camera);

    CameraQueryBO convertBO(AppCameraQueryDTO appCameraQueryDTO);

}
