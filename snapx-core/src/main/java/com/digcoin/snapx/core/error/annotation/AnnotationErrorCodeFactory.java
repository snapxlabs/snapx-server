package com.digcoin.snapx.core.error.annotation;

import com.digcoin.snapx.core.error.ErrorCodeDefinition;
import com.digcoin.snapx.core.error.ErrorCodeManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AnnotationErrorCodeFactory {

    private final ClassLoader classLoader;

    private final InvocationHandler invocationHandler;


    public AnnotationErrorCodeFactory(ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.invocationHandler = new AnnotationErrorCodeHandler();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> tClass) {

        for (Method method : tClass.getDeclaredMethods()) {
            ErrorCodeManager.getInstance().updateIfAbsent(toErrorCode(method));
        }

        return (T) Proxy.newProxyInstance(this.classLoader, new Class[]{tClass}, invocationHandler);
    }

    private ErrorCodeDefinition toErrorCode(Method method) {
        Class<?> targetInterface = method.getDeclaringClass();
        ErrorCodeGroup group = targetInterface.getAnnotation(ErrorCodeGroup.class);
        ErrorCode errorCode = method.getAnnotation(ErrorCode.class);

        return new ErrorCodeDefinition(
                group.translator(),
                group.value(),
                method.getName(),
                errorCode.code(),
                errorCode.message());
    }

}
