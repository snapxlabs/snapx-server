package com.digcoin.snapx.server.base.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberAvatarDTO {

    @Schema(description = "头像id")
    private Long avatarMaterialGalleryId;

    @Schema(description = "背景图id")
    private Long avatarCoverMaterialGalleryId;

    @Schema(description = "头像链接")
    private String avatarUrl;

    @Schema(description = "背景图链接")
    private String avatarCoverUrl;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "头像图文件id", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Long avatarResourceFileId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "背景图文件id", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Long avatarCoverResourceFileId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "排序号", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer sort;

}
