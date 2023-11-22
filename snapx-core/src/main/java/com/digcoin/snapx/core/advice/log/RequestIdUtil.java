package com.digcoin.snapx.core.advice.log;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/2 23:29
 * @description
 */
public class RequestIdUtil {

    private static final String X_REQUEST_ID = "x-request-id";

    public static void setRequestIdIfAbsent(HttpServletRequest request) {
        if (StringUtils.isNoneBlank(getRequestId())) {
            return;
        }

        String requestId = null;
        if (Objects.nonNull(request)) {
            requestId = request.getHeader(X_REQUEST_ID);
        }
        setRequestIdIfAbsent(requestId);
    }

    public static void setRequestIdIfAbsent(String requestId) {
        if (StringUtils.isNoneBlank(requestId)) {
            MDC.put(X_REQUEST_ID, requestId);
            return;
        }
        setRequestIdIfAbsent();
    }

    public static void setRequestIdIfAbsent() {
        if (StringUtils.isNoneBlank(getRequestId())) {
            return;
        }
        MDC.put(X_REQUEST_ID, UUID.randomUUID().toString());
    }

    public static String getRequestId() {
        return MDC.get(X_REQUEST_ID);
    }

    public static void clear() {
        MDC.clear();
    }

    public static void setContextMap(Map<String, String>  context) {
        MDC.setContextMap(context);
    }

}
