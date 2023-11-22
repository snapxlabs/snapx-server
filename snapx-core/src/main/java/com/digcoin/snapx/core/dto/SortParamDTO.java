package com.digcoin.snapx.core.dto;

import com.digcoin.snapx.core.common.enums.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/20 15:47
 * @description
 */
@Data
public class SortParamDTO {

    @Schema(description = "排序字段")
    private String field;

    @Schema(description = "排序方式（ASC 或 DESC）")
    private SortDirection direction;

}
