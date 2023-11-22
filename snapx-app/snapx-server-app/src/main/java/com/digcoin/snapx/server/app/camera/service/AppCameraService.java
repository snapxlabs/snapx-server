package com.digcoin.snapx.server.app.camera.service;

import com.digcoin.snapx.domain.camera.bo.CameraQueryBO;
import com.digcoin.snapx.domain.camera.entity.Camera;
import com.digcoin.snapx.domain.camera.service.CameraService;
import com.digcoin.snapx.server.app.camera.converter.AppCameraConverter;
import com.digcoin.snapx.server.app.camera.dto.AppCameraQueryDTO;
import com.digcoin.snapx.server.app.camera.vo.AppCameraVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 15:33
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppCameraService {

    private final CameraService cameraService;

    private final AppCameraConverter appCameraConverter;

    public List<AppCameraVO> listCamera(AppCameraQueryDTO dto) {
        CameraQueryBO cameraQueryBO = appCameraConverter.convertBO(dto);
        return cameraService.listCamera(cameraQueryBO).stream().map(appCameraConverter::intoVO).collect(Collectors.toList());
    }

    public AppCameraVO getCamera(Long id) {
        return Optional.ofNullable(cameraService.findCameraById(id)).map(appCameraConverter::intoVO).orElse(null);
    }

}
