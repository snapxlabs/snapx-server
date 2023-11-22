package com.digcoin.snapx.server.app.restaurant.vo;

import com.digcoin.snapx.server.app.restaurant.dto.RestaurantDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/28 21:55
 * @description
 */
@Data
public class RstPageGoogleNearbyVO {

    @Schema(description = "餐厅列表")
    private List<RestaurantDTO> list;

    @Schema(description = "下一页 Token")
    private String nextPageToken;

}
