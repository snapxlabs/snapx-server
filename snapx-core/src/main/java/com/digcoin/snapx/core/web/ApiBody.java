package com.digcoin.snapx.core.web;

import com.digcoin.snapx.core.advice.log.RequestIdUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ApiBody<T> {
    private static final String CODE_SUCCEED = "0";
    private static final String MESSAGE_SUCCEED = "success";
    @Schema(description = "接口响应状态码；0表示请求成功；非0表示请求发生异常")
    private final String code;
    @Schema(description = "接口异常响应消息")
    private final String message;
    @Schema(description = "接口返回数据")
    private final T data;
    @Schema(description = "请求 ID")
    private final String requestId;

    public ApiBody(String code,
                   String message,
                   T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.requestId = RequestIdUtil.getRequestId();
    }

    public static <T> ApiBody<T> of(T data) {
        return new ApiBody<>(CODE_SUCCEED, MESSAGE_SUCCEED, data);
    }

    public static <T> ApiBody<T> ok() {
        return new ApiBody<>(CODE_SUCCEED, MESSAGE_SUCCEED, null);
    }

    public static <T> ApiBody<T> error(String code, String message) {
        return new ApiBody<>(code, message, null);
    }

}
