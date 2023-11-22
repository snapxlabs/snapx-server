package com.digcoin.snapx.core.error.enums;

import com.digcoin.snapx.core.error.BusinessExceptionTranslator;
import com.digcoin.snapx.core.error.ErrorCodeDefinition;
import com.digcoin.snapx.core.error.ErrorCodeManager;
import com.digcoin.snapx.core.error.ExceptionTranslator;

import java.text.MessageFormat;
import java.util.Objects;

public interface EnumErrorCodeFactory<E extends Enum<E>> {

    default void update(String code, String message) {
        @SuppressWarnings("unchecked")
        ErrorCodeDefinition definition = new ErrorCodeDefinition(getTranslator(), getGroup(), ((E) this).name(), code, message);
        ErrorCodeManager.getInstance().updateIfAbsent(definition);
    }

    default RuntimeException withDefaults() {
        return this.build(null, null, null, null);
    }

    default RuntimeException withDefaults(Object... placeholder) {
        return this.build(null, null, null, placeholder);
    }

    default RuntimeException withMessage(String message) {
        return this.build(message, null, null, null);
    }

    default RuntimeException withMessage(String message, Object... placeholder) {
        return this.build(message, null, null, placeholder);
    }

    default RuntimeException withData(Object data) {
        return this.build(null, data, null, null);
    }

    default RuntimeException withData(Object data, Throwable cause) {
        return this.build(null, data, cause, null);
    }

    default RuntimeException withData(Object data, String message) {
        return this.build(message, data, null, null);
    }

    default RuntimeException withData(Object data, String message, Object... placeholder) {
        return this.build(message, data, null, placeholder);
    }

    default RuntimeException withCause(Throwable cause) {
        return this.build(null, null, cause, null);
    }

    default RuntimeException withCause(Throwable cause, Object data) {
        return this.build(null, data, cause, null);
    }

    default RuntimeException withCause(Throwable cause, String message) {
        return this.build(message, null, cause, null);
    }

    default RuntimeException withCause(Throwable cause, String message, Object... placeholder) {
        return this.build(message, null, cause, placeholder);
    }

    default RuntimeException build(String message, Object data, Throwable cause, Object... placeholder) {
        ErrorCodeDefinition errorCode = getErrorCode();
        String msg = message;
        if (Objects.nonNull(placeholder) && placeholder.length > 0) {
            if (Objects.nonNull(msg)) {
                msg = MessageFormat.format(msg, placeholder);
            } else {
                msg = MessageFormat.format(errorCode.getMessage(), placeholder);
            }
        }
        return ErrorCodeManager.exceptionBuilder()
                .errorCode(errorCode)
                .message(msg)
                .placeholder(placeholder)
                .data(data)
                .cause(cause)
                .build();
    }

    default ErrorCodeDefinition getErrorCode() {
        @SuppressWarnings("unchecked") String name = ((E) this).name();
        return ErrorCodeManager.getInstance().getErrorCode(getGroup(), name);
    }

    default String getGroup() {
        return "common";
    }

    default Class<? extends ExceptionTranslator<ErrorCodeDefinition, ? extends RuntimeException>>  getTranslator() {
        return BusinessExceptionTranslator.class;
    }
}
