package com.digcoin.snapx.server.app.infra.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 20:18
 * @description
 */
@Data
public class LocationVO {

    @Schema(description = "国家 ID")
    private Long countryId;

    @Schema(description = "国家名称")
    private String countryName;

    @Schema(description = "地点 ID")
    private Long localityId;

    @Schema(description = "地点名称")
    private String localityName;

    // @Schema(description = "当前定位城市")
    // private Boolean curr;

}
