package com.digcoin.snapx.server.admin.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/17 11:36
 * @description
 */
@Data
public class AdminActivityVO {

    @Schema(description = "活动Id")
    private Long id;

    @Schema(description = "活动名称")
    private String name;

    @Schema(description = "活动开始日期")
    private LocalDate startDate;

    @Schema(description = "活动结束日期")
    private LocalDate endDate;

    @Schema(description = "活动介绍")
    private String introduction;

    @Schema(description = "活动位置")
    private String position;

    @Schema(description = "是否精选活动")
    private Boolean isSpec;

    @Schema(description = "活动是否启用")
    private Boolean isEnabled;

    @Schema(description = "排序，越大越靠前，默认为0")
    private Integer sort;

    @Schema(description = "活动二维码base64地址")
    private String qrCodeBase64;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

    @Schema(description = "修改时间", type = "string")
    private LocalDateTime updateTime;
}
