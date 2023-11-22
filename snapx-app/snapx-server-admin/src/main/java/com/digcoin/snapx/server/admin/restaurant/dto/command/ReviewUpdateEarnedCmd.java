package com.digcoin.snapx.server.admin.restaurant.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/8 13:38
 * @description
 */
@Data
public class ReviewUpdateEarnedCmd {

    @NotNull
    @Min(0)
    @Schema(description = "评价 ID")
    private Long id;

    @NotNull
    @Schema(description = "已赚")
    private BigDecimal earned;

}
