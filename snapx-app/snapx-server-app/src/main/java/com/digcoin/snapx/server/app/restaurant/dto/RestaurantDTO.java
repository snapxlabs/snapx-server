package com.digcoin.snapx.server.app.restaurant.dto;

import com.digcoin.snapx.server.base.infra.dto.CurrencyDTO;
import com.digcoin.snapx.server.base.member.dto.BaseAvgGiftCountDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:18
 * @description
 */
@Data
public class RestaurantDTO implements Serializable {

    @Schema(description = "餐厅 ID")
    private Long id;

    @Schema(description = "国家 ID")
    private Long countryId;

    @Schema(description = "地点 ID")
    private Long localityId;

    @Schema(description = "餐厅货币信息")
    private CurrencyDTO currency;

    @Schema(description = "餐厅名称")
    private String name;

    @Schema(description = "餐厅名称（香港繁体）")
    private String nameZhHk;

    @Schema(description = "餐厅封面 URL")
    private String coverUrl;

    @Schema(description = "平均评分：1-5")
    private BigDecimal avgRating;

    @Schema(description = "平均赚取信息")
    private BaseAvgGiftCountDTO baseAvgGiftCount;

    @Schema(description = "平均赚取")
    private BigDecimal avgEarned;

    @Schema(description = "平均消费")
    private BigDecimal avgSpent;

    @Schema(description = "平均消费（美元）")
    private BigDecimal avgSpentUsd;

    @Schema(description = "收入单位")
    private String avgEarnedUnit;

    @Schema(description = "谷歌地方 ID")
    private String placeId;

    @Schema(description = "经度")
    private BigDecimal lng;

    @Schema(description = "纬度")
    private BigDecimal lat;

    @Schema(description = "评分1总数")
    private Long rating1Num;

    @Schema(description = "评分2总数")
    private Long rating2Num;

    @Schema(description = "评分3总数")
    private Long rating3Num;

    @Schema(description = "评分4总数")
    private Long rating4Num;

    @Schema(description = "评分5总数")
    private Long rating5Num;

    @Schema(description = "距离（单位:KM）")
    private BigDecimal distance;

    @Schema(description = "是否谷歌餐厅")
    private Boolean google;

    @Schema(description = "谷歌餐厅图片引用")
    private String photoReference;

    @Schema(description = "谷歌图片宽度")
    private Integer photoWidth;

    @Schema(description = "谷歌图片高度")
    private Integer photoHeight;

    @Schema(description = "是否指定餐厅")
    private Boolean spec;

    @Schema(description = "是否开启水印")
    private Boolean watermark;

    @Schema(description = "水印背景 URL")
    private String watermarkBgUrl;

    @Schema(description = "水印图标 URL")
    private String watermarkLogoUrl;

    @Schema(description = "是否已验证")
    private Boolean verified;

}
