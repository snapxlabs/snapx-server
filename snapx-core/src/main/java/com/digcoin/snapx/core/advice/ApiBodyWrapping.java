package com.digcoin.snapx.core.advice;

import com.digcoin.snapx.core.web.ApiBody;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Objects;

@Order(0)
@ControllerAdvice
public class ApiBodyWrapping implements ResponseBodyAdvice<Object> {

    private final String[] basePackages;

    public ApiBodyWrapping(String... basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        boolean basePackageMatch = true;
        Method method = returnType.getMethod();
        Class<?> methodDeclaringClass = method.getDeclaringClass();
        String packageName = methodDeclaringClass.getName();
        for (String basePackage : basePackages) {
            basePackageMatch = false;
            if (packageName.startsWith(basePackage)) {
                basePackageMatch = true;
                break;
            }
        }

        if (!basePackageMatch) {
            return false;
        }
        if (Objects.equals(StringHttpMessageConverter.class, converterType)) {
            return false;
        }
        if (Objects.equals(ByteArrayHttpMessageConverter.class, converterType)) {
            return false;
        }
        if (Objects.equals(ResponseEntity.class, method.getReturnType())) {
            return false;
        }
        if (Objects.equals(ApiBody.class, method.getReturnType())) {
            return false;
        }
        if(method.isAnnotationPresent(IgnoreApiBody.class)){
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        return ApiBody.of(body);
    }

}
