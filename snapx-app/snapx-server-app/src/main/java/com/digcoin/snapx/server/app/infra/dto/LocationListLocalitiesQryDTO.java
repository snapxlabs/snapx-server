package com.digcoin.snapx.server.app.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

import java.math.BigDecimal;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/19 13:15
 * @description
 */

@ParameterObject
@Data
public class LocationListLocalitiesQryDTO {

    @Schema(description = "城市名称关键词", required = true)
    private String keyword;

    @Schema(description = "经度", hidden = true)
    private BigDecimal lng;

    @Schema(description = "纬度", hidden = true)
    private BigDecimal lat;

}
