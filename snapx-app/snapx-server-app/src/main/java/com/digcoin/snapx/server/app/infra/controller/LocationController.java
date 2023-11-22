package com.digcoin.snapx.server.app.infra.controller;

import com.digcoin.snapx.server.app.infra.dto.LocationListLocalitiesQryDTO;
import com.digcoin.snapx.server.app.infra.dto.LocationLocateQryDTO;
import com.digcoin.snapx.server.app.infra.vo.LocationVO;
import com.digcoin.snapx.server.app.restaurant.service.AppLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 20:30
 * @description
 */
@Tag(name = "343 - 位置")
@RequiredArgsConstructor
@RestController
@RequestMapping("/infra/location")
public class LocationController {

    private final AppLocationService appLocationService;

    @Operation(summary = "定位")
    @PostMapping("locate")
    public LocationVO locate(@Valid @RequestBody LocationLocateQryDTO dto) {
        return appLocationService.locate(dto);
    }

    @Operation(summary = "获取城市列表")
    @GetMapping("list-localities")
    public List<LocationVO> listLocalities(LocationListLocalitiesQryDTO dto) {
        return appLocationService.listLocalities(dto);
    }

    @Operation(summary = "获取推荐城市列表")
    @GetMapping("list-rec-localities")
    public List<LocationVO> listRecLocalities(LocationListLocalitiesQryDTO dto) {
        return appLocationService.listRecLocalities(dto);
    }

}
