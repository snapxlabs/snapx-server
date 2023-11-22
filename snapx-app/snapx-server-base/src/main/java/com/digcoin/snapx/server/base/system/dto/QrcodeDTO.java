package com.digcoin.snapx.server.base.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QrcodeDTO {

    @Schema(description = "二维码id")
    private Long id;

    @Schema(description = "会员id")
    private Long memberId;

    /**
     * 二维码类型枚举：INVITE 邀请码
     * @see com.digcoin.snapx.domain.system.constant.QrcodeType
     */
    @Schema(description = "二维码类型枚举：INVITE 邀请码")
    private String codeType;

    @Schema(description = "使用次数限制。0表示无限制")
    private Integer useLimit;

    @Schema(description = "已使用次数")
    private Integer usedCount;

    @Schema(description = "二维码宽度")
    private Integer width;

    @Schema(description = "图片类型：例如PNG,JPEG")
    private String imageType;

    @Schema(description = "二维码包含的文本内容")
    private String content;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
