package com.digcoin.snapx.server.admin.infra.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.digcoin.snapx.domain.infra.enums.NotificationStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NotificationDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "主键", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Length(max = 255, message = "title too long")
    @Schema(description = "标题")
    private String title;

    @NotEmpty(message = "context is require")
    @Length(max = 255, message = "context too long")
    @Schema(description = "正文")
    private String context;

    @Length(max = 255, message = "description too long")
    @Schema(description = "描述，备注信息")
    private String description;

    @NotNull(message = "send inSiteMessage or not is require")
    @Schema(description = "是否发送站内信")
    private Boolean inSiteMessage;

    @NotNull(message = "send app push or not is require")
    @Schema(description = "是否发送APP推送")
    private Boolean push;

    @NotNull(message = "confirm if send global is require")
    @Schema(description = "全局推送")
    private Boolean global;

    @Schema(description = "推送会员id列表，global=false必传")
    private List<Long> memberIdList;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "推送状态： DRAFT 草稿；DONE 已推送", accessMode = Schema.AccessMode.READ_ONLY)
    private NotificationStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "创建时间", type = "date", format = "string", accessMode = Schema.AccessMode.READ_ONLY)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "更新时间", type = "date", format = "string", accessMode = Schema.AccessMode.READ_ONLY)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "创建操作人id", accessMode = Schema.AccessMode.READ_ONLY)
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "更新操作人id", accessMode = Schema.AccessMode.READ_ONLY)
    private Long updateBy;

}
