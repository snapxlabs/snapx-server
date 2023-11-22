package com.digcoin.snapx.server.admin.restaurant.dto;

import com.digcoin.snapx.server.admin.member.dto.MemberDTO;
import com.digcoin.snapx.server.base.infra.dto.CountryDTO;
import com.digcoin.snapx.server.base.infra.dto.LocalityDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/27 18:37
 * @description
 */
@Data
public class RestaurantReviewDTO {

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

    @Schema(description = "会员 ID")
    private Long memberId;

    @Schema(description = "会员")
    private MemberDTO member;

    @Schema(description = "餐厅 ID")
    private Long restaurantId;

    @Schema(description = "餐厅")
    private RestaurantDTO restaurant;

    @Schema(description = "订单 ID")
    private Long orderId;

    @Schema(description = "评分")
    private Integer rate;

    @Schema(description = "花费")
    private BigDecimal spent;

    @Schema(description = "货币代码")
    private String currencyCode;

    // @Schema(description = "花费货币")
    // private String spentCurrency;

    // @Schema(description = "钱包记录 ID")
    // private Long spentWalletRecordId;

    // @Schema(description = "照片 URL 列表")
    // private String photoUrls;

    @Schema(description = "照片 URL 列表")
    private List<String> photoUrls;

    @Schema(description = "评价内容")
    private String content;

    @Schema(description = "奖励金额")
    private BigDecimal earned;

    // @Schema(description = "钱包（$PIC）记录 ID")
    // private Long earnedWalletRecordId;

    @Schema(description = "点赞数")
    private Long likesNum;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", type = "string")
    private LocalDateTime updateTime;

    @Schema(description = "创建人 ID")
    private Long createBy;

    @Schema(description = "创建人")
    private MemberDTO creator;

    // @Schema(description = "更新人 ID")
    // private Long updateBy;

}
