package com.digcoin.snapx.server.app.infra.dto;

import com.digcoin.snapx.domain.infra.enums.InSiteMessageSubject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InSiteMessageDTO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "站内信主题枚举：点赞 THUMBS_UP；NEW_COMMENT；邀请码被使用 FRIEND_SIGNED_UP")
    private InSiteMessageSubject subject;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "正文")
    private String context;

    @Schema(description = "关联的id")
    private String relateId;

    @Schema(description = "接收人会员id")
    private Long toMemberId;

    @Schema(description = "状态值：0 未读；1已读")
    private Integer status;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", type = "string")
    private LocalDateTime updateTime;

}
