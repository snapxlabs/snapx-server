package com.digcoin.snapx.server.admin.camera.dto;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/30 1:16
 * @description
 */
@Data
public class AdminFilmMemberDetailPageDTO extends Pageable {

    @NotNull(message = "Member Id Can Not Be Null")
    @Schema(description = "会员Id", required = true)
    private Long memberId;

}
