package com.digcoin.snapx.core.error.annotation;

import com.digcoin.snapx.core.error.ErrorCodeManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AnnotationErrorCodeHandler implements InvocationHandler {

    private static final BiFunction<String, String, String> keyGenerator = (scope, name) -> scope + "." + name;
    private final Map<String, ErrorCodeMetadata> errorCodeMetadataCache = new ConcurrentHashMap<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(ErrorCode.class)) {
            return method.invoke(proxy, args);
        }

        Parameter parameter = extractParameter(method, args);
        ErrorCodeMetadata metadata = toErrorCodeMetadata(method);
        return ErrorCodeManager.exceptionBuilder()
                .errorCode(metadata.group, metadata.name)
                .message(parameter.message)
                .cause(parameter.errorCause)
                .data(parameter.errorData)
                .placeholder(parameter.placeholder)
                .build();
    }

    private Parameter extractParameter(Method method, Object[] args) {
        String message = null;
        Throwable errorCause = null;
        Object errorData = null;
        Object[] msgTplParams = null;
        if (args != null) {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            int messagePosition = -1;
            int causePosition = -1;
            int dataPosition = -1;
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (Objects.equals(annotation.annotationType(), ErrorMessage.class)) {
                        messagePosition = i;
                        break;
                    }
                    if (Objects.equals(annotation.annotationType(), ErrorCause.class)) {
                        causePosition = i;
                        break;
                    }
                    if (Objects.equals(annotation.annotationType(), ErrorData.class)) {
                        dataPosition = i;
                        break;
                    }
                }
            }

            int msgTplParamsLength = args.length;
            if (messagePosition >= 0) {
                message = String.valueOf(args[messagePosition]);
                msgTplParamsLength--;
            }
            if (dataPosition >= 0) {
                errorData = args[dataPosition];
                msgTplParamsLength--;
            }
            if (causePosition >= 0) {
                errorCause = (Throwable) args[causePosition];
                msgTplParamsLength--;
            } else if (args[args.length - 1] instanceof Throwable) {
                causePosition = args.length - 1;
                errorCause = (Throwable) args[causePosition];
                msgTplParamsLength--;
            }

            msgTplParams = new Object[msgTplParamsLength];
            for (int i = 0, j = 0; i < args.length; i++) {
                if (i == dataPosition || i == messagePosition || i == causePosition) {
                    continue;
                }
                msgTplParams[j++] = args[i];
            }
        }
        return new Parameter(message, msgTplParams, errorData, errorCause);
    }

    private ErrorCodeMetadata toErrorCodeMetadata(Method method) {
        return withCache(ErrorCodeMetadata::from).apply(method);
    }

    private Function<Method, ErrorCodeMetadata> withCache(Function<Method, ErrorCodeMetadata> target) {
        return method -> errorCodeMetadataCache.computeIfAbsent(
                keyGenerator.apply(method.getDeclaringClass().getName(), method.getName()),
                k -> target.apply(method));
    }

    private static class Parameter {

        final String message;
        final Object[] placeholder;
        final Object errorData;
        final Throwable errorCause;

        public Parameter(String message, Object[] placeholder, Object errorData, Throwable errorCause) {
            this.message = message;
            this.placeholder = placeholder;
            this.errorData = errorData;
            this.errorCause = errorCause;
        }
    }

    private static class ErrorCodeMetadata {

        final String group;
        final String name;

        public static ErrorCodeMetadata from(Method method) {
            ErrorCodeGroup group = method.getDeclaringClass().getAnnotation(ErrorCodeGroup.class);
            return new ErrorCodeMetadata(group.value(), method.getName());
        }

        public ErrorCodeMetadata(String group, String name) {
            this.group = group;
            this.name = name;
        }
    }

}
