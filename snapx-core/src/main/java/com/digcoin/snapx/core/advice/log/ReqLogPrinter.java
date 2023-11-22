package com.digcoin.snapx.core.advice.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/3 09:31
 * @description
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ReqLogPrinter {

    private final ObjectMapper objectMapper;

    private ThreadLocal<Long> runtimeThreadLocal = new ThreadLocal<>();

    private static HashMap<String, String> getRequestHeaders(HttpServletRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }

        HashMap<String, String> headers = new HashMap<>(16);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            headers.put(key, request.getHeader(key));
        }
        return headers;
    }

    private static HashMap<String, String> getResponseHeaders(HttpServletResponse response) {
        if (Objects.isNull(response)) {
            return null;
        }

        HashMap<String, String> headers = new HashMap<>(16);
        Collection<String> headerNames = response.getHeaderNames();
        for (String name : headerNames) {
            headers.put(name, response.getHeader(name));
        }
        return headers;
    }

    public void printRequest(HttpServletRequest request, String body) {
        try {
            runtimeThreadLocal.set(System.currentTimeMillis());

            String method = null;
            String requestURL = null;
            String contentType = null;
            String headers = objectMapper.writeValueAsString(getRequestHeaders(request));
            String remoteAddress = null;
            if (Objects.nonNull(request)) {
                method = request.getMethod();
                requestURL = request.getRequestURL().toString();
                String queryString = request.getQueryString();
                if (StringUtils.isNoneBlank(queryString)) {
                    requestURL = String.format("%s?%s", requestURL, queryString);
                }
                remoteAddress = request.getRemoteAddr();
                contentType = request.getContentType();
            }

            log.info("Request Info");
            log.info("==> Request Method      : {}", method);
            log.info("==> Request URL         : {}", requestURL);
            log.info("==> Request Body        : {}", body);
            log.info("==> Request Content-Type: {}", contentType);
            log.info("==> Request Headers     : {}", headers);
            log.info("==> Request IP          : {}", remoteAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printResponse(HttpServletResponse response, Object body) {
        try {
            Integer statusCode = null;
            String bodyStr = Optional.ofNullable(objectMapper.writeValueAsString(body)).orElse("");
            String headers = objectMapper.writeValueAsString(getResponseHeaders(response));
            if (Objects.nonNull(response)) {
                statusCode = response.getStatus();
            }
            long runtime = System.currentTimeMillis() - Optional.ofNullable(runtimeThreadLocal.get()).orElse(0L);

            log.info("Response Info");
            log.info("<== Response Status : {}", statusCode);
            log.info("<== Response Body   : {}", bodyStr);
            log.info("<== Response Size   : {}", bodyStr.length());
            log.info("<== Response Headers: {}", headers);
            log.info("<== Response Runtime: {}", String.format("%sms", runtime));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
