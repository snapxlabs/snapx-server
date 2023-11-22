package com.digcoin.snapx.server.app.restaurant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.restaurant.bo.RestaurantReviewPageReviewsQryDTO;
import com.digcoin.snapx.server.app.restaurant.dto.LikesDTO;
import com.digcoin.snapx.server.app.restaurant.dto.RestaurantReviewDTO;
import com.digcoin.snapx.server.app.restaurant.dto.command.query.PageLikesQry;
import com.digcoin.snapx.server.app.restaurant.dto.command.query.RestaurantReviewFindReviewQry;
import com.digcoin.snapx.server.app.restaurant.dto.command.RestaurantReviewLikeCmd;
import com.digcoin.snapx.server.app.restaurant.dto.command.RestaurantReviewUpdateCmd;
import com.digcoin.snapx.server.app.restaurant.service.AppRestaurantReviewService;
import com.digcoin.snapx.server.app.restaurant.vo.RestaurantReviewLikeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:18
 * @description
 */
@Tag(name = "312 - 餐厅评价")
@SaCheckLogin
@RequiredArgsConstructor
@RestController
@RequestMapping("/ret/restaurant/review")
public class AppRestaurantReviewController {

    private final AppRestaurantReviewService appRestaurantReviewService;

    @Deprecated
    @SaIgnore
    @Operation(summary = "分页查询评价列表")
    @GetMapping("page-reviews")
    public PageResult<RestaurantReviewDTO> pageReviewsDeprecated(@Schema(hidden = true) CurrentUser currentUser, @ParameterObject RestaurantReviewPageReviewsQryDTO dto) {
        return appRestaurantReviewService.pageReviews(dto, currentUser);
    }

    @SaIgnore
    @Operation(summary = "分页查询评价列表")
    @PostMapping("page-reviews")
    public PageResult<RestaurantReviewDTO> pageReviews(@Schema(hidden = true) CurrentUser currentUser, @Valid @RequestBody RestaurantReviewPageReviewsQryDTO dto) {
        return appRestaurantReviewService.pageReviews(dto, currentUser);
    }

    @SaIgnore
    @Operation(summary = "分页查询点赞列表")
    @PostMapping("page-likes")
    public PageResult<LikesDTO> pageLikes(@Valid @RequestBody PageLikesQry qry) {
        return appRestaurantReviewService.pageLikes(qry);
    }

    @SaIgnore
    @Operation(summary = "获取评价详情")
    @GetMapping("find-review")
    public RestaurantReviewDTO findReview(@Schema(hidden = true) CurrentUser currentUser, RestaurantReviewFindReviewQry qry) {
        return appRestaurantReviewService.findReview(qry, currentUser);
    }

    @Operation(summary = "点赞")
    @PostMapping("like")
    public RestaurantReviewLikeVO like(@Schema(hidden = true) CurrentUser currentUser, @Valid @RequestBody RestaurantReviewLikeCmd cmd) {
        return appRestaurantReviewService.like(cmd, currentUser);
    }

    @Operation(summary = "更新评分和内容")
    @PostMapping("update-rate-and-content")
    public RestaurantReviewDTO updateRateAndContent(@Schema(hidden = true) CurrentUser currentUser, @Valid @RequestBody RestaurantReviewUpdateCmd cmd) {
        return appRestaurantReviewService.updateRateAndContent(cmd, currentUser);
    }

}
