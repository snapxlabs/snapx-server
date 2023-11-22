package com.digcoin.snapx.server.admin.camera.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/23 0:42
 * @description
 */
@Data
public class AdminFilmMemberVO {

    @Schema(description = "会员胶卷ID")
    private Long id;

    @Schema(description = "会员Id")
    private Long memberId;

    @Schema(description = "会员账号")
    private String memberAccount;

    @Schema(description = "会员昵称")
    private String memberNickName;

    @Schema(description = "当前可用数量")
    private Long remainingQuantity;

    @Schema(description = "累计获取数量")
    private Long totalQuantity;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", type = "string")
    private LocalDateTime updateTime;


}
