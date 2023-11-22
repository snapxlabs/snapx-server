package com.digcoin.snapx.server.app.camera.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 17:40
 * @description
 */
@Data
public class AppCameraMemberVO {

    @Schema(description = "会员相机ID")
    private Long id;

    @Schema(description = "会员Id")
    private Long memberId;

    @Schema(description = "当前相机经验")
    private Long experience;

    @Schema(description = "当前相机等级")
    private Long currentLevel;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", type = "string")
    private LocalDateTime updateTime;

    @Schema(description = "相机信息")
    private AppCameraVO appCameraVO;

}
