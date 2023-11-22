package com.digcoin.snapx.server.admin.restaurant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.server.admin.restaurant.dto.RestaurantReviewDTO;
import com.digcoin.snapx.server.admin.restaurant.dto.command.RestaurantReviewCreateReviewCmd;
import com.digcoin.snapx.server.admin.restaurant.dto.command.RestaurantReviewUpdateReviewCmd;
import com.digcoin.snapx.server.admin.restaurant.dto.command.ReviewDeleteReviewCmd;
import com.digcoin.snapx.server.admin.restaurant.dto.command.ReviewUpdateEarnedCmd;
import com.digcoin.snapx.server.admin.restaurant.dto.command.query.RestaurantFindReviewQry;
import com.digcoin.snapx.server.admin.restaurant.dto.command.query.ReviewPageReviewQry;
import com.digcoin.snapx.server.admin.restaurant.service.AdminRestaurantReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/27 18:36
 * @description
 */
@Tag(name = "230327 - 餐厅/评价")
@Slf4j
@SaCheckLogin
@Validated
@RestController
@RequestMapping("/restaurant/review")
@RequiredArgsConstructor
public class AdminRestaurantReviewController {

    private final AdminRestaurantReviewService adminRestaurantReviewService;

    @Operation(summary = "分页查询餐厅评价列表")
    @PostMapping("page-reviews")
    public PageResult<RestaurantReviewDTO> pageReviews(@Valid @RequestBody ReviewPageReviewQry qry) {
        return adminRestaurantReviewService.pageReviews(qry);
    }

    @Operation(summary = "获取评价详情")
    @PostMapping("find-review")
    public RestaurantReviewDTO findReview(@Valid @RequestBody RestaurantFindReviewQry qry) {
        return adminRestaurantReviewService.findReview(qry);
    }

    @Operation(summary = "添加评价")
    @PostMapping("create-review")
    public RestaurantReviewDTO updateReview(@Valid @RequestBody RestaurantReviewCreateReviewCmd cmd) {
        return adminRestaurantReviewService.createReview(cmd);
    }

    @Operation(summary = "更新评价")
    @PostMapping("update-review")
    public RestaurantReviewDTO updateReview(@Valid @RequestBody RestaurantReviewUpdateReviewCmd cmd) {
        return adminRestaurantReviewService.updateReview(cmd);
    }

    @Operation(summary = "删除评价")
    @PostMapping("delete-review")
    public Boolean deleteReview(@Valid @RequestBody ReviewDeleteReviewCmd cmd) {
        return adminRestaurantReviewService.deleteReview(cmd);
    }

    @Operation(summary = "更新已赚")
    @PostMapping("update-earned")
    public Boolean updateEarned(@Valid @RequestBody ReviewUpdateEarnedCmd cmd, @Schema(hidden = true) CurrentUser currentUser) {
        return adminRestaurantReviewService.updateEarned(cmd, currentUser);
    }

}
