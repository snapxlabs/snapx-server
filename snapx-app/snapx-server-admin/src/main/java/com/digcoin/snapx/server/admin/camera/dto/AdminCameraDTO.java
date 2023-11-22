package com.digcoin.snapx.server.admin.camera.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 16:28
 * @description
 */
@Data
public class AdminCameraDTO {

    @NotBlank(message = "Camera name cannot be null")
    @Schema(description = "相机名称")
    @Length(max = 50, message = "Camera name is too long")
    private String name;

    @NotBlank(message = "Camera code cannot be null")
    @Schema(description = "相机编号")
    @Length(max = 20, message = "Camera code is too long")
    private String code;

    @NotBlank(message = "Camera picture cannot be null")
    @Schema(description = "相机图片地址")
    @Length(max = 255, message = "Camera picture is too long")
    private String pictureUrl;

    @Schema(description = "幸运属性")
    @Min(value = 0, message = "Camera luck can't be less tha 0")
    private Long luck;

    @Schema(description = "效率属性")
    @Min(value = 0, message = "Camera efficiency can't be less tha 0")
    private Long efficiency;

    @Schema(description = "恢复属性")
    @Min(value = 0, message = "Camera resilience can't be less tha 0")
    private Long resilience;

    @Schema(description = "稳定属性")
    @Min(value = 0, message = "Camera stability can't be less tha 0")
    private Long stability;

    @Schema(description = "电池属性")
    @Min(value = 0, message = "Camera battery can't be less tha 0")
    private Long battery;

    @Schema(description = "内存属性")
    @Min(value = 0, message = "Camera memory can't be less tha 0")
    private Long memory;

    @Schema(description = "货币属性")
    @Min(value = 0, message = "Camera mint can't be less tha 0")
    private Long mint;

    @Schema(description = "保养属性")
    @Min(value = 0, message = "Camera maintain can't be less tha 0")
    private Long maintain;

    @NotNull(message = "Camera isGift cannot be null")
    @Schema(description = "是否注册赠送")
    private Boolean isGift;

    @Schema(description = "相机描述介绍")
    @Length(max = 500, message = "Camera instruction is too long")
    private String instruction;

    @Schema(description = "排序，越大越靠前")
    private Integer sort;

}
