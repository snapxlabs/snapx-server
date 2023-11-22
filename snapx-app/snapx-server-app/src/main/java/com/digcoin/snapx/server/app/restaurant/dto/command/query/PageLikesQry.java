package com.digcoin.snapx.server.app.restaurant.dto.command.query;

import com.digcoin.snapx.core.dto.SortParamDTO;
import com.digcoin.snapx.core.mybatis.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/18 15:58
 * @description
 */
@Data
public class PageLikesQry extends Pageable implements Serializable {

    @NotNull(message = "reviewId cannot be null")
    @Schema(description = "评价 ID")
    private Long reviewId;

    @Schema(description = "排序：create_time=按点赞时间（默认按最新）")
    private List<SortParamDTO> orderBy;

    @Getter
    public enum OrderByField {

        CREATE_TIME("create_time", "按点赞时间（默认按最新）");

        private final String field;

        private final String desc;

        OrderByField(String field, String desc) {
            this.field = field;
            this.desc = desc;
        }
    }

}
