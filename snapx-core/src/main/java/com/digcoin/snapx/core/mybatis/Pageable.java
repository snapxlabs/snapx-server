package com.digcoin.snapx.core.mybatis;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class Pageable {

    @Schema(description = "页码，第一页从1开始计算，默认值为1", type = "integer", defaultValue = "1")
    @NotNull(message = "页码不能为空!")
    @Min(value = 1, message = "页码不能小于1")
    protected Integer page = 1;

    @Schema(description = "页面大小，默认值为10", type = "integer", defaultValue = "10")
    @NotNull(message = "页面大小不能为空!")
    @Min(value = 1, message = "页面大小不能小于1")
    protected Integer pageSize = 10;

}
