package com.digcoin.snapx.server.app.camera.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 15:34
 * @description
 */
@Data
public class AppCameraQueryDTO {

    @Schema(description = "相机名称")
    private String name;
}
