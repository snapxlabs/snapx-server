package com.digcoin.snapx.server.base.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/5 22:53
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MultilevelLocalityDTO {

    @Schema(description = "类型")
    private Type type;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "完整名称")
    private String longName;

    @Schema(description = "缩略名称")
    private String shortName;

    @Schema(description = "ID")
    private Long value;

    @Schema(description = "完整名称")
    private String label;

    @Schema(description = "下级")
    private List<MultilevelLocalityDTO> children;

    public enum Type {

        COUNTRY, LOCALITY

    }

}
