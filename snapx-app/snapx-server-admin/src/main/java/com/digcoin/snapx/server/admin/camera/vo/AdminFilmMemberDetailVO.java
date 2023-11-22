package com.digcoin.snapx.server.admin.camera.vo;

import com.digcoin.snapx.domain.camera.enums.FilmChangeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/30 1:08
 * @description
 */
@Data
public class AdminFilmMemberDetailVO {

    @Schema(description = "会员胶卷ID")
    private Long id;

    @Schema(description = "会员Id")
    private Long memberId;

    @Schema(description = "来源类型")
    private FilmChangeType filmChangeType;

    @Schema(description = "变动数量")
    private Long variableQuantity;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", type = "string")
    private LocalDateTime updateTime;
}
