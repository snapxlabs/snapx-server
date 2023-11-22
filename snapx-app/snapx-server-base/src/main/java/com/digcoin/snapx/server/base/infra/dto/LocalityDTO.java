package com.digcoin.snapx.server.base.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/25 23:25
 * @description
 */
@Data
public class LocalityDTO {

    @Schema(description = "国家")
    private CountryDTO country;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "完整名称")
    private String longName;

    @Schema(description = "缩写名称")
    private String shortName;

}
