package com.digcoin.snapx.server.admin.camera.vo;

import com.digcoin.snapx.domain.camera.enums.CameraSource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/23 0:23
 * @description
 */
@Data
public class AdminCameraMemberVO {

    @Schema(description = "会员相机ID")
    private Long id;

    @Schema(description = "会员账号")
    private String memberAccount;

    @Schema(description = "会员昵称")
    private String memberNickName;

    @Schema(description = "相机名称")
    private String cameraName;

    @Schema(description = "相机编号")
    private String cameraCode;

    @Schema(description = "相机来源")
    private CameraSource cameraSource;

    @Schema(description = "当前相机经验")
    private Long experience;

    @Schema(description = "当前相机等级")
    private Long currentLevel;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", type = "string")
    private LocalDateTime updateTime;



}
