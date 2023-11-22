package com.digcoin.snapx.server.admin.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.system.bo.ContentInformationQuery;
import com.digcoin.snapx.server.admin.system.service.ContentInformationAppService;
import com.digcoin.snapx.server.base.system.dto.ContentInformationDTO;
import com.digcoin.snapx.server.base.system.dto.ContentInformationItemDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "内容资讯管理")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/system/content-information")
@RequiredArgsConstructor
public class ContentInformationController {

    private final ContentInformationAppService contentInformationAppService;

    @Operation(summary = "分页获取内容资讯列表")
    @GetMapping("page-content-information")
    PageResult<ContentInformationItemDTO> pageContentInformation(ContentInformationQuery query) {
        return contentInformationAppService.pageContentInformation(query);
    }

    @Operation(summary = "获取内容资讯详情")
    @GetMapping("find-content-information/{id}")
    ContentInformationDTO findContentInformation(@Schema(description = "内容资讯id") @PathVariable Long id) {
        return contentInformationAppService.findContentInformation(id);
    }

    @Operation(summary = "创建内容资讯")
    @PostMapping("create-content-information")
    public ContentInformationDTO createContentInformation(@RequestBody ContentInformationDTO contentInformation) {
        return contentInformationAppService.createContentInformation(contentInformation);
    }

    @Operation(summary = "更新内容资讯")
    @PostMapping("update-content-information/{id}")
    public ContentInformationDTO updateContentInformation(@Schema(description = "内容资讯id") @PathVariable Long id,
                                                          @RequestBody ContentInformationDTO contentInformation) {
        return contentInformationAppService.updateContentInformation(id, contentInformation);
    }

    @Operation(summary = "更新内容资讯发布状态")
    @PostMapping("update-content-information-publish/{id}")
    public ContentInformationDTO updateContentInformationPublish(@Schema(description = "内容资讯id") @PathVariable Long id,
                                                                 @Schema(description = "发布状态：true 已发布；false 待发布") @RequestParam Boolean publish) {
        return contentInformationAppService.updateContentInformationPublish(id, publish);
    }


    @Operation(summary = "删除指定内容资讯")
    @PostMapping("delete-content-information/{id}")
    public ContentInformationDTO deleteContentInformation(@Schema(description = "内容资讯id") @PathVariable Long id) {
        return contentInformationAppService.deleteContentInformation(id);
    }

}
