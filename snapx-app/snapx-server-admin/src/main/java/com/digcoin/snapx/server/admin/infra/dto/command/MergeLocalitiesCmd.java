package com.digcoin.snapx.server.admin.infra.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/12 13:49
 * @description
 */
@Data
public class MergeLocalitiesCmd {

    @Schema(description = "源 ID")
    private Long sourceId;

    @Schema(description = "目标 ID")
    private Long targetId;

}
