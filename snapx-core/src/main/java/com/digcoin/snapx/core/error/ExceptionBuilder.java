package com.digcoin.snapx.core.error;

import java.text.MessageFormat;
import java.util.Objects;

public class ExceptionBuilder {

    private final ErrorCodeManager errorCodeManager;

    private ErrorCodeDefinition errorCode;

    private String message;

    private Throwable cause;

    private Object data;

    private Object[] placeholder;

    public ExceptionBuilder(ErrorCodeManager errorCodeManager) {
        this.errorCodeManager = errorCodeManager;
    }

    public ExceptionBuilder errorCode(String scope, String name) {
        this.errorCode = errorCodeManager.getErrorCode(scope, name);
        return this;
    }

    public ExceptionBuilder errorCode(ErrorCodeDefinition errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public ExceptionBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ExceptionBuilder cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    public ExceptionBuilder data(Object data) {
        this.data = data;
        return this;
    }

    public ExceptionBuilder placeholder(Object... placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public RuntimeException build() {
        ExceptionTranslator<ErrorCodeDefinition, ? extends RuntimeException> translator;
        Class<? extends ExceptionTranslator<ErrorCodeDefinition, ? extends RuntimeException>> translatorClass = errorCode.getTranslatorClass();
        if (Objects.isNull(translatorClass)) {
            translator = new BusinessExceptionTranslator();
        } else {
            try {
                translator = translatorClass.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        String msg = this.message;
        if (Objects.nonNull(this.placeholder) && this.placeholder.length > 0) {
            if (Objects.isNull(msg)) {
                msg = MessageFormat.format(this.errorCode.getMessage(), this.placeholder);
            } else {
                msg = MessageFormat.format(this.message, placeholder);
            }
        }

        return this.errorCode.into(translator.with(msg, this.cause, this.data));
    }
}
