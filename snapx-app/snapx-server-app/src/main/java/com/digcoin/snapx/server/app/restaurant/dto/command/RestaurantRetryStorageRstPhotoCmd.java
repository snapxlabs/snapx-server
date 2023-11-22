package com.digcoin.snapx.server.app.restaurant.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:12
 * @description
 */
@Data
public class RestaurantRetryStorageRstPhotoCmd {

    @Schema(description = "餐厅 ID 列表", required = true)
    private List<Long> ids;

}
