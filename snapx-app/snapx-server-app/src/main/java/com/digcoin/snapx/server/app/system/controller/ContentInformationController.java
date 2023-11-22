package com.digcoin.snapx.server.app.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.digcoin.snapx.server.app.system.service.ContentInformationAppService;
import com.digcoin.snapx.server.base.system.dto.ContentInformationDTO;
import com.digcoin.snapx.server.base.system.dto.ContentInformationItemDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "800 - 系统 - 内容资讯")
@Slf4j
@Validated
@RestController
@RequestMapping("/system/content-information")
@RequiredArgsConstructor
public class ContentInformationController {

    private final ContentInformationAppService contentInformationAppService;

    @SaIgnore
    @Operation(summary = "获取内容资讯列表")
    @GetMapping("list-publish-content-information")
    public List<ContentInformationItemDTO> listPublishContentInformation(@Schema(description = "内容资讯类型：NOTICE 系统公告") String type) {
        return contentInformationAppService.listPublishContentInformation(type, 100);
    }

    @SaIgnore
    @Operation(summary = "获取内容资讯详情")
    @GetMapping("get-content-information/{id}")
    public ContentInformationDTO getContentInformation(@Schema(description = "内容资讯id") @PathVariable Long id) {
        return contentInformationAppService.getContentInformation(id);
    }

}
