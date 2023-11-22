package com.digcoin.snapx.server.admin.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/17 11:35
 * @description
 */
@Data
public class AdminActivityDTO {

    @NotBlank(message = "Activity name can not be null")
    @Schema(description = "活动名称")
    private String name;

    @Schema(description = "社区活动开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "社区活动结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(description = "社区活动介绍")
    private String introduction;

    @NotBlank(message = "Position can not be null")
    @Schema(description = "活动位置",required = true)
    private String position;

    @Schema(description = "活动是否启用")
    private Boolean isEnabled;

    @Schema(description = "是否精选活动")
    private Boolean isSpec;

    @Schema(description = "排序，越大越靠前，默认为0")
    private Integer sort;

}
