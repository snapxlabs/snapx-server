package com.digcoin.snapx.server.app.restaurant.dto.command;

import com.digcoin.snapx.domain.restaurant.bo.LatLngDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:12
 * @description
 */
@Data
public class OrderCreateOrderCmd {

    // @NotNull(message = "localityId cannot be null")
    // @Min(value = 1, message = "localityId cannot be less than 1")
    // @Schema(description = "地点 ID", required = true)
    // private Long localityId;

    @Valid
    @NotNull(message = "latLng cannot be null")
    @Schema(description = "经纬度", required = true)
    private LatLngDTO latLng;

    // @NotNull(message = "restaurantId cannot be null")
    // @Min(value = 1, message = "restaurantId cannot be less than 1")
    @Schema(description = "餐厅 ID")
    private Long restaurantId;

    @Valid
    // @NotNull(message = "placeId cannot be null")
    // @Min(value = 1, message = "placeId cannot be less than 1")
    @Schema(description = "地点信息")
    private PlaceDTO place;

    @NotBlank(message = "photoUrls cannot be empty")
    @Schema(description = "照片地址（英文逗号分割）", required = true)
    private String photoUrls;

    @NotNull(message = "rating cannot be null")
    @Min(value = 1, message = "rating cannot be less than 1")
    @Schema(description = "评分：1-5", required = true)
    private Integer rating;

    @NotNull(message = "spend cannot be null")
    @Min(value = 1, message = "spend cannot be less than 0")
    @Schema(description = "花费", required = true)
    private BigDecimal spend;

    @NotNull(message = "currencyCode cannot be null")
    @Schema(description = "货币代码", required = true)
    private String currencyCode;

    @Schema(description = "评价内容")
    private String content;

    @Data
    public static class PlaceDTO {

        @NotNull
        @Schema(description = "谷歌地方 ID", required = true)
        private String id;

        @NotNull
        @Schema(description = "名称", required = true)
        private String name;

        @Schema(description = "名称（香港繁体）")
        private String nameZhHk;

        @Schema(description = "封面地址")
        private String coverUrl;

        @Schema(description = "谷歌图片引用")
        private String photoReference;

        @Schema(description = "图片宽度")
        private Integer photoWidth;

        @Schema(description = "图片高度")
        private Integer photoHeight;

    }

}
