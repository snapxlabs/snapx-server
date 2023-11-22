package com.digcoin.snapx.server.admin.restaurant.dto.command.query;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/8 10:45
 * @description
 */
@Data
public class RestaurantPageRestaurantsQry extends Pageable {

    @Schema(description = "餐厅 ID")
    private Long id;

    @Schema(description = "国家 ID")
    private Long countryId;

    @Schema(description = "地点 ID")
    private Long localityId;

    @Schema(description = "餐厅名称")
    private String name;

    @Schema(description = "谷歌餐厅")
    private Boolean google;

    @Schema(description = "指定餐厅")
    private Boolean spec;

    @Schema(description = "是否已验证")
    private Boolean verified;

    @Schema(description = "是否开启水印")
    private Boolean watermark;

}
