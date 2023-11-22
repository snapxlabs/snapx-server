package com.digcoin.snapx.server.admin.camera.converter;

import com.digcoin.snapx.domain.camera.bo.CameraQueryBO;
import com.digcoin.snapx.domain.camera.entity.Camera;
import com.digcoin.snapx.server.admin.camera.dto.AdminCameraDTO;
import com.digcoin.snapx.server.admin.camera.dto.AdminCameraPageDTO;
import com.digcoin.snapx.server.admin.camera.vo.AdminCameraVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 16:28
 * @description
 */
@Mapper(componentModel = "spring")
public interface AdminCameraConverter {

    Camera fromDTO(AdminCameraDTO dto);

    CameraQueryBO convertBO(AdminCameraPageDTO dto);

    AdminCameraVO intoVO(Camera camera);

}
