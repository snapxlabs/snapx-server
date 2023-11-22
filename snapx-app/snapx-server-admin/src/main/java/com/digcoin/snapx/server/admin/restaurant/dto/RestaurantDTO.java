package com.digcoin.snapx.server.admin.restaurant.dto;

import com.digcoin.snapx.server.admin.member.dto.MemberDTO;
import com.digcoin.snapx.server.base.infra.dto.CountryDTO;
import com.digcoin.snapx.server.base.infra.dto.LocalityDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/25 21:45
 * @description
 */
@Data
public class RestaurantDTO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "国家 ID")
    private Long countryId;

    @Schema(description = "国家")
    private CountryDTO country;

    @Schema(description = "地点 ID")
    private Long localityId;

    @Schema(description = "地点")
    private LocalityDTO locality;

    @Schema(description = "餐厅名称")
    private String name;

    @Schema(description = "餐厅封面 URL")
    private String coverUrl;

    @Schema(description = "平均赚取")
    private String avgEarned;

    @Schema(description = "平均评分：1-5")
    private BigDecimal avgRating;

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

    @Schema(description = "谷歌地方 ID")
    private String placeId;

    @Schema(description = "经度")
    private BigDecimal lng;

    @Schema(description = "纬度")
    private BigDecimal lat;

    @Schema(description = "评价人数")
    private Long reviewNum;

    @Schema(description = "访问量")
    private Long views;

    @Schema(description = "是否已验证")
    private Integer isVerified;

    @Schema(description = "是否谷歌餐厅")
    private Boolean google;

    @Schema(description = "谷歌餐厅图片引用")
    private String photoReference;

    @Schema(description = "图片宽度")
    private Integer photoWidth;

    @Schema(description = "图片高度")
    private Integer photoHeight;

    @Schema(description = "指定餐厅")
    private Boolean spec;

    @Schema(description = "是否开启水印")
    private Boolean watermark;

    @Schema(description = "水印背景 URL")
    private String watermarkBgUrl;

    @Schema(description = "水印图标 URL")
    private String watermarkLogoUrl;

    @Schema(description = "是否已验证")
    private Boolean verified;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人 ID")
    private Long createBy;

    @Schema(description = "更新人 ID")
    private Long updateBy;

    @Schema(description = "创建人")
    private MemberDTO creator;

}
