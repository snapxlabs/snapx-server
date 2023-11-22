package com.digcoin.snapx.server.app.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/18 15:57
 * @description
 */
@Data
public class LikesDTO {

    @Schema(description = "用户 ID")
    private Long memberId;

    @Schema(description = "用户昵称")
    private String memberNickname;

    @Schema(description = "用户头像")
    private String memberAvatar;

    @Schema(description = "创建时间", type = "string")
    private LocalDateTime createTime;

}
