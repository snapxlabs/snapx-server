package com.digcoin.snapx.server.app.camera.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.digcoin.snapx.domain.camera.bo.CameraQueryBO;
import com.digcoin.snapx.domain.camera.entity.Camera;
import com.digcoin.snapx.domain.camera.entity.CameraMember;
import com.digcoin.snapx.domain.camera.enums.CameraSource;
import com.digcoin.snapx.domain.camera.service.CameraMemberService;
import com.digcoin.snapx.domain.camera.service.CameraService;
import com.digcoin.snapx.server.app.camera.converter.AppCameraConverter;
import com.digcoin.snapx.server.app.camera.converter.AppCameraMemberConverter;
import com.digcoin.snapx.server.app.camera.vo.AppCameraMemberVO;
import com.digcoin.snapx.server.app.camera.vo.AppCameraVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 17:22
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppCameraMemberService {

    private final CameraMemberService cameraMemberService;

    private final CameraService cameraService;

    private final AppCameraMemberConverter appCameraMemberConverter;

    private final AppCameraConverter appCameraConverter;

    public List<AppCameraMemberVO> listCameraMember(Long memberId) {
        List<CameraMember> cameraMembers = cameraMemberService.listCameraMember(memberId);
        if(CollectionUtils.isEmpty(cameraMembers)){
            return Collections.emptyList();
        }
        CameraQueryBO bo = new CameraQueryBO();
        bo.setIds(cameraMembers.stream().map(CameraMember::getCameraId).collect(Collectors.toSet()));
        List<Camera> cameras = cameraService.listCamera(bo);
        Map<Long, AppCameraVO> cameraMapping = cameras.stream().map(appCameraConverter::intoVO).collect(Collectors.toMap(AppCameraVO::getId, Function.identity()));
        return cameraMembers.stream().map(cameraMember -> {
            AppCameraMemberVO vo = appCameraMemberConverter.intoVO(cameraMember);
            vo.setAppCameraVO(cameraMapping.get(cameraMember.getCameraId()));
            return vo;
        }).collect(Collectors.toList());
    }

    public void createCameraMember(Long memberId, CameraSource cameraSource) {
        List<Camera> cameras = cameraService.listCamera(new CameraQueryBO()).stream()
                .filter(camera -> Boolean.TRUE.equals(camera.getIsGift())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(cameras)){
            return;
        }
        List<CameraMember> cameraMembers = cameras.stream().map(camera -> {
            CameraMember cameraMember = new CameraMember();
            cameraMember.setCameraId(camera.getId());
            cameraMember.setMemberId(memberId);
            cameraMember.setCameraSource(cameraSource);
            return cameraMember;
        }).collect(Collectors.toList());
        cameraMemberService.batchSaveCameraMember(cameraMembers);
    }
}
