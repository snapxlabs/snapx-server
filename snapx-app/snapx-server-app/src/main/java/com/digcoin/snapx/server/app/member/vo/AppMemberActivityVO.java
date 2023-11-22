package com.digcoin.snapx.server.app.member.vo;

import com.digcoin.snapx.server.app.member.dto.MemberDTO;
import com.digcoin.snapx.server.app.system.vo.AppActivityVO;
import com.digcoin.snapx.server.base.member.dto.BaseGiftCountDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/5 16:10
 * @description
 */
@Data
public class AppMemberActivityVO {

    @Schema(description = "会员活动Id")
    private Long id;

    @Schema(description = "会员信息")
    private MemberDTO member;

    @Schema(description = "活动信息")
    private AppActivityVO activity;

    @Schema(description = "参与活动时纬度")
    private String lat;

    @Schema(description = "参与活动时经度")
    private String lng;

    @Schema(description = "赠送详情")
    private BaseGiftCountDTO baseGiftCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
