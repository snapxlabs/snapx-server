package com.digcoin.snapx.server.admin.member.dto;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/6 14:37
 * @description
 */
@Data
public class AdminMemberActivityPageDTO extends Pageable {

    @Schema(description = "会员账号")
    private String memberAccount;

    @Schema(description = "相机名称")
    private String activityName;

}
