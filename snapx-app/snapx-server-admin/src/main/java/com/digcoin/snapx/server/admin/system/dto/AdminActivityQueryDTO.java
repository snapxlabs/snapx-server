package com.digcoin.snapx.server.admin.system.dto;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/17 11:35
 * @description
 */
@Data
public class AdminActivityQueryDTO extends Pageable {

    @Schema(description = "活动名称")
    private String name;

    @Schema(description = "是否精选活动")
    private Boolean isSpec;

}
