package com.digcoin.snapx.server.app.camera.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 15:32
 * @description
 */
@Data
public class AppCameraVO {

    @Schema(description = "相机ID")
    private Long id;

    @Schema(description = "相机名称")
    private String name;

    @Schema(description = "相机编号")
    private String code;

    @Schema(description = "相机图片地址")
    private String pictureUrl;

    @Schema(description = "幸运")
    private Long luck;

    @Schema(description = "效率")
    private Long efficiency;

    @Schema(description = "恢复")
    private Long resilience;

    @Schema(description = "稳定")
    private Long stability;

    @Schema(description = "电池")
    private Long battery;

    @Schema(description = "内存")
    private Long memory;

    @Schema(description = "铸造")
    private Long mint;

    @Schema(description = "保养")
    private Long maintain;

    @Schema(description = "是否注册赠送")
    private Boolean isGift;

    @Schema(description = "相机描述介绍")
    private String instruction;

    @Schema(description = "排序，越大越靠前")
    private Integer sort;
}
