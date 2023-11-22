package com.digcoin.snapx.core.advice.log;

import cn.hutool.core.io.IoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/2 18:08
 * @description
 */
@Slf4j
@Order(1)
@RequiredArgsConstructor
@ControllerAdvice
public class LogRequestBodyAdvice implements RequestBodyAdvice {

    private final ReqLogPrinter reqLogPrinter;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        String body = null;

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(attributes)) {
            try {
                body = IoUtil.read(inputMessage.getBody(), Charset.defaultCharset());
            } catch (Exception e) {
                e.printStackTrace();
            }

            HttpServletRequest request = attributes.getRequest();
            String contentType = request.getContentType();
            boolean isPostJson = HttpMethod.POST.matches(request.getMethod()) && contentType.toLowerCase().startsWith("application/json");
            if (isPostJson) {
                reqLogPrinter.printRequest(request, body);
            }
        }

        return new ReqLogHttpInputMessage(IoUtil.toStream(body, Charset.defaultCharset()), inputMessage.getHeaders());
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
