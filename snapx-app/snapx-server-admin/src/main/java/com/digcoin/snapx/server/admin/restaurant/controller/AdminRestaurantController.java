package com.digcoin.snapx.server.admin.restaurant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.server.admin.restaurant.dto.command.*;
import com.digcoin.snapx.server.admin.restaurant.dto.command.query.RestaurantFindRestaurantQry;
import com.digcoin.snapx.server.admin.restaurant.dto.command.query.RestaurantPageRestaurantsQry;
import com.digcoin.snapx.server.admin.restaurant.service.AdminRestaurantService;
import com.digcoin.snapx.server.admin.restaurant.dto.RestaurantDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/25 21:41
 * @description
 */
@Tag(name = "230325 - 餐厅")
@Slf4j
@SaCheckLogin
@Validated
@RestController
@RequestMapping("/restaurant/restaurant")
@RequiredArgsConstructor
public class AdminRestaurantController {

    private final AdminRestaurantService adminRestaurantService;

    @Operation(summary = "分页查询餐厅列表")
    @PostMapping("page-restaurants")
    public PageResult<RestaurantDTO> pageRestaurants(@Valid @RequestBody RestaurantPageRestaurantsQry qry) {
        return adminRestaurantService.pageRestaurants(qry);
    }

    @Operation(summary = "删除餐厅")
    @PostMapping("delete-restaurant")
    public Boolean deleteRestaurant(@Valid @RequestBody RestaurantDeleteRestaurantCmd cmd) {
        return adminRestaurantService.deleteRestaurant(cmd);
    }

    @Operation(summary = "指定餐厅")
    @PostMapping("set-spec")
    public Boolean setSpec(@Valid @RequestBody RestaurantSetSpecCmd cmd) {
        return adminRestaurantService.setSpec(cmd);
    }

    @Operation(summary = "开启水印")
    @PostMapping("enable-watermark")
    public Boolean enableWatermark(@Valid @RequestBody RestaurantEnableWatermarkCmd cmd) {
        return adminRestaurantService.enableWatermark(cmd);
    }

    @Operation(summary = "餐厅验证")
    @PostMapping("set-verified")
    public Boolean setVerified(@Valid @RequestBody RestaurantSetVerifiedCmd cmd) {
        return adminRestaurantService.setVerified(cmd);
    }

    @Operation(summary = "创建餐厅")
    @PostMapping("create-restaurant")
    public RestaurantDTO createRestaurant(@Valid @RequestBody RestaurantCreateRestaurantCmd cmd) {
        return adminRestaurantService.createRestaurant(cmd);
    }

    @Operation(summary = "获取餐厅详情")
    @PostMapping("find-restaurant")
    public RestaurantDTO findRestaurant(@Valid @RequestBody RestaurantFindRestaurantQry qry) {
        return adminRestaurantService.findRestaurant(qry);
    }

    @Operation(summary = "更新餐厅")
    @PostMapping("update-restaurant")
    public RestaurantDTO updateRestaurant(@Valid @RequestBody RestaurantUpdateRestaurantCmd cmd) {
        return adminRestaurantService.updateRestaurant(cmd);
    }

}
