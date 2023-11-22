package com.digcoin.snapx.domain.restaurant.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 20:18
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LatLngDTO implements Serializable {

    @NotNull(message = "lng cannot be null")
    @Schema(description = "经度", required = true)
    private BigDecimal lng;

    @NotNull(message = "lat cannot be null")
    @Schema(description = "纬度", required = true)
    private BigDecimal lat;

}
