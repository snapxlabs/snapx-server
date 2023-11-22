package com.digcoin.snapx.server.admin.camera.dto;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/21 23:30
 * @description
 */
@Data
public class AdminCameraPageDTO extends Pageable {

    @Schema(description = "相机名称")
    private String name;

    @Schema(description = "相机编号")
    private String code;

    @Schema(description = "是否注册赠送")
    private Boolean isGift;
}
