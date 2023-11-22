package com.digcoin.snapx.core.advice;

import com.digcoin.snapx.core.web.ApiAttachment;
import com.digcoin.snapx.core.web.ApiAttachmentProvider;
import com.digcoin.snapx.core.web.ApiBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Order(1)
@ControllerAdvice
public class ApiAttachmentWrapping implements ResponseBodyAdvice<Object> {

    @Autowired(required = false)
    private List<ApiAttachmentProvider> apiAttachmentProviderList = new LinkedList<>();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = returnType.getMethod();
        if(Objects.nonNull(method) && method.isAnnotationPresent(Attach.class)){
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        Attach methodAnnotation = returnType.getMethodAnnotation(Attach.class);
        if (Objects.isNull(methodAnnotation)) {
            return body;
        }
        String name = methodAnnotation.value();

        Optional<ApiAttachmentProvider> provider = getApiAttachmentProvider(name);
        if (provider.isPresent()) {
            ApiAttachmentProvider apiAttachmentProvider = provider.get();
            Object attachment = apiAttachmentProvider.getAttachment(body,
                    returnType,
                    selectedContentType,
                    selectedConverterType,
                    request,
                    response);
            @SuppressWarnings("unchecked") ApiBody apiBody = (ApiBody) body;
            return ApiAttachment.of(apiBody, attachment);
        } else {
            return body;
        }
    }

    private Optional<ApiAttachmentProvider> getApiAttachmentProvider(String name) {
        return apiAttachmentProviderList
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
