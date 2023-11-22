package com.digcoin.snapx.server.app.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PhoneAreaCodeDTO {

    @Schema(description = "地区英文名")
    private String englishName;

    @Schema(description = "地区中文名")
    private String chineseName;

    @Schema(description = "地区代码")
    private String countryCode;

    @Schema(description = "电话代码")
    private String phoneCode;

    @Schema(description = "国旗图标")
    private String emoji;

}
