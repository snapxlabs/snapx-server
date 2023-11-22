package com.digcoin.snapx.domain.system.bo;

import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;

@Data
@ParameterObject
public class ContentInformationQuery extends Pageable {

    @Schema(description = "内容资讯类型枚举：NOTICE 公告")
    private String informationType;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "是否发布状态：0 未发布；1 已发布；")
    private Boolean publish;

}
