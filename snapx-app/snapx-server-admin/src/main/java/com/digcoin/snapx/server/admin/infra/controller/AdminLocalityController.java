package com.digcoin.snapx.server.admin.infra.controller;

import com.digcoin.snapx.server.admin.infra.dto.command.MergeLocalitiesCmd;
import com.digcoin.snapx.server.base.infra.dto.LocalityDTO;
import com.digcoin.snapx.server.base.infra.dto.MultilevelLocalityDTO;
import com.digcoin.snapx.server.base.infra.service.BaseLocalityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/5 15:32
 * @description
 */
@Tag(name = "230405 - 城市")
@Slf4j
// @SaCheckLogin
@Validated
@RestController
@RequestMapping("/infra/locality")
@RequiredArgsConstructor
public class AdminLocalityController {

    private final BaseLocalityService baseLocalityService;

    @Operation(summary = "获取城市列表")
    @GetMapping("list-localities")
    public List<LocalityDTO> listLocalities() {
        return baseLocalityService.listLocalities();
    }

    @Operation(summary = "获取多级城市列表")
    @GetMapping("list-multilevel-localities")
    public List<MultilevelLocalityDTO> listMultilevelLocalities() {
        return baseLocalityService.listMultilevelLocalities();
    }

    @Operation(summary = "合并城市")
    @PostMapping("merge-localities")
    public Boolean mergeLocalities(@Valid @RequestBody MergeLocalitiesCmd cmd) {
        return baseLocalityService.mergeLocalities(cmd.getSourceId(), cmd.getTargetId());
    }

}
