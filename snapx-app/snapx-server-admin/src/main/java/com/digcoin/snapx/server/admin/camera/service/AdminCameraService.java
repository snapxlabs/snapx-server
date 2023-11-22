package com.digcoin.snapx.server.admin.camera.service;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.camera.entity.Camera;
import com.digcoin.snapx.domain.camera.error.CameraError;
import com.digcoin.snapx.domain.camera.service.CameraService;
import com.digcoin.snapx.server.admin.camera.converter.AdminCameraConverter;
import com.digcoin.snapx.server.admin.camera.dto.AdminCameraDTO;
import com.digcoin.snapx.server.admin.camera.dto.AdminCameraPageDTO;
import com.digcoin.snapx.server.admin.camera.vo.AdminCameraVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 16:26
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCameraService {

    private final CameraService cameraService;

    private final AdminCameraConverter adminCameraConverter;

    public void createCamera(AdminCameraDTO dto) {
        Camera camera = adminCameraConverter.fromDTO(dto);
        cameraService.saveCamera(camera);
    }

    public PageResult<AdminCameraVO> pageCamera(AdminCameraPageDTO dto) {
        PageResult<Camera> pageResult = cameraService.pageCamera(adminCameraConverter.convertBO(dto));
        return PageResult.fromPageResult(pageResult, adminCameraConverter::intoVO);
    }

    public AdminCameraVO getCamera(Long id) {
        return Optional.ofNullable(cameraService.findById(id)).map(
                adminCameraConverter::intoVO).orElseThrow(CameraError.CAMERA_NOT_EXISTS::withDefaults);
    }

    public void editCamera(Long id, AdminCameraDTO dto) {
        Optional<Camera> optional = Optional.ofNullable(cameraService.findById(id));
        if (optional.isEmpty()) {
            throw CameraError.CAMERA_NOT_EXISTS.withDefaults();
        }
        Camera camera = adminCameraConverter.fromDTO(dto);
        camera.setId(id);
        cameraService.updateCamera(camera);
    }

    public void deleteCamera(Long id) {
        cameraService.deleteCamera(id);
    }
}
