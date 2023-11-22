package com.digcoin.snapx.server.admin.camera.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/21 23:35
 * @description
 */
@Data
public class AdminCameraVO {

    @Schema(description = "ID")
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

    @Schema(description = "创建时间" , type = "string")
    private LocalDateTime createTime;

    @Schema(description = "更新时间" , type = "string")
    private LocalDateTime updateTime;

}
