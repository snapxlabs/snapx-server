package com.digcoin.snapx.server.admin.infra.controller;

import com.digcoin.snapx.server.base.infra.dto.ResourceFileDTO;
import com.digcoin.snapx.server.base.infra.service.ResourceFileAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件管理")
@RequiredArgsConstructor
@RequestMapping("/infra/resource-file")
@RestController
public class ResourceFileController {

    private final ResourceFileAppService resourceFileAppService;

    @Operation(summary = "上传文件")
    @PostMapping(value = "/create-resource-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResourceFileDTO createResourceFile(@Schema(description = "要上传的文件", type = "string", format = "binary")
                                                  @RequestPart("file") MultipartFile multipartFile) {
        return resourceFileAppService.createResourceFile(multipartFile);
    }

    @Operation(summary = "获取上传文件详细信息")
    @GetMapping("/find-resource-file/{id}")
    public ResourceFileDTO findResourceFile(@Schema(description = "文件id", type = "string") @PathVariable("id") Long id) {
        return resourceFileAppService.findResourceFile(id);
    }

}
