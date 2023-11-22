package com.digcoin.snapx.domain.infra.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import com.digcoin.snapx.domain.infra.enums.NotificationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

@Data
@ParameterObject
public class NotificationQuery extends Pageable {


    @Schema(description = "标题")
    private String title;

    @Schema(description = "正文")
    private String context;

    @Schema(description = "推送状态： DRAFT 草稿；DONE 已推送")
    private NotificationStatus status;

}
