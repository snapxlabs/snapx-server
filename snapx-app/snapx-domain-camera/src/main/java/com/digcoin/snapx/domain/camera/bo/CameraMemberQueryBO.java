package com.digcoin.snapx.domain.camera.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/23 0:28
 * @description
 */
@Data
public class CameraMemberQueryBO extends Pageable {

    @Schema(description = "相机名称")
    private String cameraName;

    @Schema(description = "相机编号")
    private String cameraCode;

    @Schema(description = "会员账号")
    private String memberAccount;

    @Schema(description = "会员名称")
    private String memberNickName;
}
