package com.digcoin.snapx.domain.system.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/17 16:15
 * @description
 */
@Data
public class ActivityQueryBO extends Pageable {

    @Schema(description = "活动名称")
    private String name;

    @Schema(description = "是否精选活动")
    private Boolean isSpec;

}
