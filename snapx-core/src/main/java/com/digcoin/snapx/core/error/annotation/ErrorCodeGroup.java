package com.digcoin.snapx.core.error.annotation;

import com.digcoin.snapx.core.error.ErrorCodeDefinition;
import com.digcoin.snapx.core.error.BusinessExceptionTranslator;
import com.digcoin.snapx.core.error.ExceptionTranslator;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ErrorCodeGroup {

    String value() default "common";

    Class<? extends ExceptionTranslator<ErrorCodeDefinition, ? extends RuntimeException>> translator() default BusinessExceptionTranslator.class;

}
