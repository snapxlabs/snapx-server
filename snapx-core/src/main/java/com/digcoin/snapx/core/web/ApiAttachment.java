package com.digcoin.snapx.core.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiAttachment<T, A>{

    private String code;

    private String msg;

    private T data;

    private A attachment;

    public ApiAttachment(ApiBody<T> apiBody, A attachment) {
        this(
                apiBody.getCode(),
                apiBody.getMessage(),
                apiBody.getData(),
                attachment
        );
    }

    @SuppressWarnings("unchecked")
    public static <T, A> ApiAttachment<T, A> of (ApiBody<T> apiBody, A attachment) {
        return new ApiAttachment(apiBody, attachment);
    }

}
