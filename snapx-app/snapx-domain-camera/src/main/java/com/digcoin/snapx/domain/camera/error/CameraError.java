package com.digcoin.snapx.domain.camera.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

public enum CameraError implements EnumErrorCodeFactory {

    CAMERA_NOT_EXISTS("101000", "Camera not exists.");

    CameraError(String code, String message) {
        this.update(code, message);
    }

}
