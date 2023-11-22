package com.digcoin.snapx.domain.member.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/6 15:04
 * @description
 */
@Data
public class MemberActivityQueryBO extends Pageable {

    @Schema(description = "会员账号")
    private String memberAccount;

    @Schema(description = "活动名称")
    private String activityName;

    @Schema(description = "会员Id")
    private Long memberId;
}
