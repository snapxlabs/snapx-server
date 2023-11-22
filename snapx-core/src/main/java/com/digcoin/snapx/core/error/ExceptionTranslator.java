package com.digcoin.snapx.core.error;

import java.util.function.Function;

public interface ExceptionTranslator<T, R> {

    R doTranslator(T errorCode, String message, Throwable cause, Object data);

    default Function<T, R> asDefault() {
        return errorCode -> doTranslator(errorCode, null, null, null);
    }

    default Function<T, R> with(String message) {
        return errorCode -> doTranslator(errorCode, message, null, null);
    }

    default Function<T, R> with(Throwable cause) {
        return errorCode -> doTranslator(errorCode, null, cause, null);
    }

    default Function<T, R> with(Object data) {
        return errorCode -> doTranslator(errorCode, null,null, data);
    }

    default Function<T, R> with(String message, Throwable cause) {
        return errorCode -> doTranslator(errorCode, message, cause, null);
    }

    default Function<T, R> with(String message, Object data) {
        return errorCode -> doTranslator(errorCode, message, null, data);
    }

    default Function<T, R> with(Throwable cause, Object data) {
        return errorCode -> doTranslator(errorCode, null, cause, data);
    }

    default Function<T, R> with(String message, Throwable cause, Object data) {
        return errorCode -> doTranslator(errorCode, message, cause, data);
    }

}
