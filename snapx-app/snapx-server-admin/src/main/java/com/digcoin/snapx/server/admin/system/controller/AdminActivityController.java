package com.digcoin.snapx.server.admin.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.web.ApiBody;
import com.digcoin.snapx.server.admin.system.dto.AdminActivityDTO;
import com.digcoin.snapx.server.admin.system.dto.AdminActivityQueryDTO;
import com.digcoin.snapx.server.admin.system.service.AdminActivityService;
import com.digcoin.snapx.server.admin.system.vo.AdminActivityVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/1/17 17:51
 * @description 社区活动管理
 */
@Tag(name = "活动管理")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/system/activity")
@RequiredArgsConstructor
public class AdminActivityController {

    private final AdminActivityService adminActivityService;

    @Operation(summary = "创建活动")
    @PostMapping("/create")
    public void createActivity(@Validated @RequestBody AdminActivityDTO dto) {
        adminActivityService.createActivity(dto);
    }

    @Operation(summary = "活动分页")
    @GetMapping("/page")
    public PageResult<AdminActivityVO> pageActivity(AdminActivityQueryDTO dto) {
        return adminActivityService.pageActivity(dto);
    }

    @Operation(summary = "获取单个活动详情")
    @GetMapping("/{id}")
    public AdminActivityVO getActivity(@Validated @NotNull(message = "Id cannot be null")
                                 @Schema(description = "社区活动Id", required = true)
                                 @PathVariable("id") Long id) {
        return adminActivityService.getActivity(id);
    }

    @Operation(summary = "删除活动")
    @PostMapping("/delete/{id}")
    public void deleteActivity(@Validated @NotNull(message = "Id cannot be null")
                            @Schema(description = "社区活动Id", required = true)
                            @PathVariable("id") Long id) {
        adminActivityService.deleteActivity(id);
    }

    @Operation(summary = "修改活动")
    @PostMapping("/edit/{id}")
    public void editActivity(@Validated @NotNull(message = "Id cannot be null")
                          @Schema(description = "活动Id", required = true)
                          @PathVariable("id") Long id, @Validated @RequestBody AdminActivityDTO dto) {
        adminActivityService.editActivity(id, dto);
    }

    @Operation(summary = "获取单个活动svg图片")
    @GetMapping("/svg/{id}")
    public ApiBody<String> getActivitySvg(@Validated @NotNull(message = "活动Id不能为空")
                                       @Schema(description = "社区活动Id", required = true)
                                       @PathVariable("id") Long id) {
        return ApiBody.of(adminActivityService.getActivitySvg(id));
    }

}
