package com.digcoin.snapx.server.base.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentInformationDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "主键", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "内容资讯类型枚举：NOTICE 公告")
    private String informationType;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "富文本内容")
    private String content;

    @Schema(description = "是否发布状态：0 未发布；1 已发布；")
    private Boolean publish;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "排序号，默认值1000，步进1000", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer sort;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "创建时间", type = "string", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "更新时间", type = "string", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;

}
