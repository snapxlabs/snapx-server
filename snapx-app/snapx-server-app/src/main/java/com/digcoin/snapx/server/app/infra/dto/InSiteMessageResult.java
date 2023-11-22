package com.digcoin.snapx.server.app.infra.dto;

import com.digcoin.snapx.core.mybatis.PageResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class InSiteMessageResult {

    @Schema(description = "未读消息数量")
    private Long unread;

    @Schema(description = "站内信列表")
    private PageResult<InSiteMessageDTO> page;

    public static InSiteMessageResult create(Long unread, PageResult<InSiteMessageDTO> page) {
        InSiteMessageResult result = new InSiteMessageResult();
        result.setUnread(unread);
        result.setPage(page);
        return result;
    }

}
