package com.digcoin.snapx.domain.infra.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import com.digcoin.snapx.domain.infra.enums.InSiteMessageSubject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

@Data
@ParameterObject
public class InSiteMessageQuery extends Pageable {

    @Schema(description = "当前登录用户id", hidden = true)
    private Long toMemberId;

    @Schema(description = "站内信主题枚举：点赞 THUMBS_UP；NEW_COMMENT；邀请码被使用 FRIEND_SIGNED_UP")
    private InSiteMessageSubject subject;

    @Schema(description = "状态值：0 未读；1已读")
    private Integer status;

    @Schema(description = "搜索关键词")
    private String search;

}
