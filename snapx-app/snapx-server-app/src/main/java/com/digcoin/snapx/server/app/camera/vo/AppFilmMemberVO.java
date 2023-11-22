package com.digcoin.snapx.server.app.camera.vo;

import com.digcoin.snapx.domain.camera.enums.FilmChangeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/23 16:49
 * @description
 */
@Data
public class AppFilmMemberVO {

    @Schema(description = "会员胶卷ID")
    private Long id;

    @Schema(description = "会员Id")
    private Long memberId;

    @Schema(description = "当前可用数量")
    private Long remainingQuantity;

    @Schema(description = "总获取数量")
    private Long totalQuantity;

    @Schema(description = "数量变动明细")
    private List<AppFilmMemberDetailVO> detailVOList;

    @Data
    public static class AppFilmMemberDetailVO{

        @Schema(description = "来源类型 REGISTER_GIFT INTERACTION_GIFT INVITE_GIFT PURCHASE PHOTOGRAPH")
        private FilmChangeType filmChangeType;

        @Schema(description = "当次变动数量，正数表示增加 负数表示减少")
        private Long variableQuantity;

        @Schema(description = "创建时间", type = "string")
        private LocalDateTime createTime;

        @Schema(description = "更新时间", type = "string")
        private LocalDateTime updateTime;
    }


}
