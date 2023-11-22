package com.digcoin.snapx.server.app.restaurant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.restaurant.bo.RestaurantPageNearbyRestaurantsQryDTO;
import com.digcoin.snapx.domain.restaurant.bo.RestaurantPageGoogleNearbyRestaurantsQryDTO;
import com.digcoin.snapx.domain.restaurant.bo.RestaurantPageRestaurantsQryDTO;
import com.digcoin.snapx.server.app.restaurant.dto.command.RestaurantCreateRestaurantCmd;
import com.digcoin.snapx.server.app.restaurant.dto.command.query.RestaurantFindRestaurantQry;
import com.digcoin.snapx.server.app.restaurant.dto.command.RestaurantRetryStorageRstPhotoCmd;
import com.digcoin.snapx.server.app.restaurant.service.AppRestaurantService;
import com.digcoin.snapx.server.app.restaurant.dto.RestaurantDTO;
import com.digcoin.snapx.server.app.restaurant.vo.RstPageGoogleNearbyVO;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "311 - 餐厅")
@SaCheckLogin
@RequiredArgsConstructor
@RequestMapping("/ret/restaurant")
@RestController
public class AppRestaurantController {

    private final AppRestaurantService appRestaurantService;

    @SaIgnore
    @PostMapping("retry-storage-rst-photo")
    public void retryStorageRstPhoto(@Valid @RequestBody RestaurantRetryStorageRstPhotoCmd cmd) {
        appRestaurantService.retryStorageRstPhoto(cmd);
    }

    @Deprecated
    @SaIgnore
    @Operation(summary = "分页查询餐厅列表")
    @GetMapping("page-restaurants")
    public PageResult<RestaurantDTO> pageRestaurantsDeprecated(@ParameterObject RestaurantPageRestaurantsQryDTO dto) {
        return appRestaurantService.pageRestaurants(dto);
    }

    @SaIgnore
    @Operation(summary = "分页查询餐厅列表")
    @PostMapping("page-restaurants")
    public PageResult<RestaurantDTO> pageRestaurants(@Valid @RequestBody RestaurantPageRestaurantsQryDTO dto) {
        return appRestaurantService.pageRestaurants(dto);
    }

    @SaIgnore
    @Operation(summary = "分页获取附近餐厅列表")
    @GetMapping("page-nearby-restaurants")
    public PageResult<RestaurantDTO> pageNearbyRestaurants(RestaurantPageNearbyRestaurantsQryDTO dto) {
        return appRestaurantService.pageNearbyRestaurants(dto);
    }

    @SaIgnore
    @Operation(summary = "分页谷歌附近餐厅")
    @GetMapping("page-google-nearby-restaurants")
    public RstPageGoogleNearbyVO listNearbyRestaurants(RestaurantPageGoogleNearbyRestaurantsQryDTO dto) {
        return appRestaurantService.pageGoogleNearbyRestaurants(dto);
    }

    @SaIgnore
    @Operation(summary = "获取餐厅详情")
    @GetMapping("find-restaurant")
    public RestaurantDTO findRestaurant(RestaurantFindRestaurantQry qry) {
        return appRestaurantService.findRestaurant(qry);
    }

    @Operation(summary = "创建餐厅")
    @PostMapping("create-restaurant")
    public RestaurantDTO createRestaurant(@Valid @RequestBody RestaurantCreateRestaurantCmd cmd) {
        return appRestaurantService.createRestaurant(cmd);
    }

}
