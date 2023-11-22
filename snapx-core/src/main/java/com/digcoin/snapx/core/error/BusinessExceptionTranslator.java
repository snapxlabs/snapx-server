package com.digcoin.snapx.core.error;

public class BusinessExceptionTranslator implements ExceptionTranslator<ErrorCodeDefinition, BusinessException> {

    @Override
    public BusinessException doTranslator(ErrorCodeDefinition errorCode, String message, Throwable cause, Object data) {
        return new BusinessException(errorCode.getCode(), errorCode.orMessage(message), cause, data);
    }
}
