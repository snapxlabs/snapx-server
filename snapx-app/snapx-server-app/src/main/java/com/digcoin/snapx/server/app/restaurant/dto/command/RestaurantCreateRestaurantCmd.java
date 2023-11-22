package com.digcoin.snapx.server.app.restaurant.dto.command;

import com.digcoin.snapx.domain.restaurant.bo.LatLngDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:38
 * @description
 */
@Data
public class RestaurantCreateRestaurantCmd {

    @Schema(description = "谷歌地方 ID")
    private String placeId;

    @Valid
    @NotNull(message = "latLng cannot be null")
    @Schema(description = "经纬度", required = true)
    private LatLngDTO latLng;

    @NotBlank(message = "name cannot be empty")
    @Schema(description = "餐厅名称", required = true)
    private String name;

    @Schema(description = "封面地址")
    private String coverUrl;

}
