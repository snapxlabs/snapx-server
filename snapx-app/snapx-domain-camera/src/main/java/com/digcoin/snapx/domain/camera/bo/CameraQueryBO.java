package com.digcoin.snapx.domain.camera.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 16:04
 * @description
 */
@Data
public class CameraQueryBO extends Pageable {

    @Schema(description = "相机名字")
    private String name;

    @Schema(description = "相机编码")
    private String code;

    @Schema(description = "是否注册赠送")
    private Boolean isGift;

    @Schema(description = "相机Id集合")
    private Set<Long> ids;

}
