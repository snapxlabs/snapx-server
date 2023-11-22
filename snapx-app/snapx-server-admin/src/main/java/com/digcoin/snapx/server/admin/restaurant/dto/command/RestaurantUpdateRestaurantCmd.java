package com.digcoin.snapx.server.admin.restaurant.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/8 10:47
 * @description
 */
@Data
public class RestaurantUpdateRestaurantCmd {

    @NotNull
    @Min(0)
    @Schema(description = "餐厅 ID")
    private Long id;

    @NotNull(message = "countryId cannot be null")
    @Min(0)
    @Schema(description = "国家 ID")
    private Long countryId;

    @NotNull(message = "localityId cannot be null")
    @Min(value = 0, message = "")
    @Schema(description = "地点 ID")
    private Long localityId;

    @NotBlank(message = "name cannot be empty")
    @Schema(description = "餐厅名称")
    private String name;

    @Schema(description = "餐厅封面 URL")
    private String coverUrl;

    @Schema(description = "指定餐厅")
    private Boolean spec;

    @Schema(description = "是否开启水印")
    private Boolean watermark;

    @Schema(description = "水印背景 URL")
    private String watermarkBgUrl;

    @Schema(description = "水印图标 URL")
    private String watermarkLogoUrl;

    @Schema(description = "谷歌地方 ID")
    private String placeId;

    @NotNull(message = "lng cannot be null")
    @Schema(description = "经度")
    private BigDecimal lng;

    @NotNull(message = "lat cannot be null")
    @Schema(description = "纬度")
    private BigDecimal lat;

    @Schema(description = "是否谷歌餐厅")
    private Boolean google;

    @Schema(description = "谷歌餐厅图片引用")
    private String photoReference;

    @Schema(description = "图片宽度")
    private Integer photoWidth;

    @Schema(description = "图片高度")
    private Integer photoHeight;

}
