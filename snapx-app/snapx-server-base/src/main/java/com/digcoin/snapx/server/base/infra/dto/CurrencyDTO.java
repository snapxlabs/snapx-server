package com.digcoin.snapx.server.base.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/3 22:32
 * @description
 */
@Data
public class CurrencyDTO {

    @Schema(description = "货币名称")
    private String name;

    @Schema(description = "货币代码")
    private String code;

    @Schema(description = "货币符号")
    private String symbol;

}
