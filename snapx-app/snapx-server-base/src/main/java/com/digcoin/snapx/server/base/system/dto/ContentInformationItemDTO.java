package com.digcoin.snapx.server.base.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentInformationItemDTO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "内容资讯类型枚举：NOTICE 公告")
    private String informationType;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "是否发布状态：0 未发布；1 已发布；")
    private Boolean publish;

    @Schema(description = "排序号，默认值1000，步进1000")
    private Integer sort;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
