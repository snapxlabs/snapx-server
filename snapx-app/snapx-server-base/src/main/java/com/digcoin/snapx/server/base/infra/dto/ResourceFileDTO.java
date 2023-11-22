package com.digcoin.snapx.server.base.infra.dto;

import com.digcoin.snapx.core.common.constant.Format;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceFileDTO {

    @Schema(description = "文件id")
    private Long id;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "文件扩展名")
    private String fileExtension;

    @Schema(description = "文件大小，字节数")
    private Long fileSize;

    @Schema(description = "文件的http协议Content-Type")
    private String contentType;

    @Schema(description = "文件URL地址")
    private String fileUrl;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "创建时间", type = "string", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "更新时间", type = "string", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;

}
