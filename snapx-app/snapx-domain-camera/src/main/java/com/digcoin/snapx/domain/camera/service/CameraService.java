package com.digcoin.snapx.domain.camera.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.camera.bo.CameraQueryBO;
import com.digcoin.snapx.domain.camera.entity.Camera;
import com.digcoin.snapx.domain.camera.mapper.CameraMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 15:27
 * @description
 *
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CameraService {

    private final CameraMapper cameraMapper;

    public List<Camera> listCamera(CameraQueryBO cameraQueryBO) {
        LambdaQueryWrapper<Camera> cameraLambdaQueryWrapper = Wrappers.<Camera>lambdaQuery()
                .in(CollectionUtils.isNotEmpty(cameraQueryBO.getIds()), Camera::getId, cameraQueryBO.getIds())
                .eq(StringUtils.isNotBlank(cameraQueryBO.getName()), Camera::getName, cameraQueryBO.getName())
                .orderBy(Boolean.TRUE, Boolean.FALSE, Camera::getSort, Camera::getCreateTime);
        return cameraMapper.selectList(cameraLambdaQueryWrapper);
    }

    public Camera findCameraById(Long id) {
        return cameraMapper.selectById(id);
    }

    public void saveCamera(Camera camera) {
        cameraMapper.insert(camera);
    }

    public PageResult<Camera> pageCamera(CameraQueryBO cameraQueryBO) {
        return PageResult.fromPage(cameraMapper.selectPage(PageHelper.getPage(cameraQueryBO), Wrappers.lambdaQuery(Camera.class)
                .like(StringUtils.isNotBlank(cameraQueryBO.getName()), Camera::getName, cameraQueryBO.getName())
                .like(StringUtils.isNotBlank(cameraQueryBO.getCode()), Camera::getCode, cameraQueryBO.getCode())
                .eq(Objects.nonNull(cameraQueryBO.getIsGift()), Camera::getIsGift, cameraQueryBO.getIsGift())
                .orderByDesc(Camera::getSort)
                .orderByDesc(Camera::getCreateTime)
        ), Function.identity());
    }

    public Camera findById(Long id) {
        return cameraMapper.selectById(id);
    }

    public void updateCamera(Camera camera) {
        cameraMapper.updateById(camera);
    }

    public void deleteCamera(Long id) {
        cameraMapper.deleteById(id);
    }
}
