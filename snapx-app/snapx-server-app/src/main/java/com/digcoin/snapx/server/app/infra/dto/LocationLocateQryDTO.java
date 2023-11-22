package com.digcoin.snapx.server.app.infra.dto;

import com.digcoin.snapx.domain.restaurant.bo.LatLngDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 20:18
 * @description
 */
@Data
public class LocationLocateQryDTO {

    @Valid
    @NotNull
    @Schema(description = "经纬度", required = true)
    private LatLngDTO latLng;

}
