package com.digcoin.snapx.core.error;

import java.util.Objects;

public class BusinessException extends RuntimeException {

    private final String code;

    private final Object data;

    public BusinessException(String code, String message, Throwable cause, Object data) {
        super(message, cause);
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return Objects.isNull(this.data) ? null : (T) this.data;
    }
}
