package com.digcoin.snapx.domain.camera.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/30 1:17
 * @description
 */
@Data
public class FilmMemberDetailQueryBO extends Pageable {

    @Schema(description = "会员Id")
    private Long memberId;
}
