package com.digcoin.snapx.core.advice.log;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/2 16:49
 * @description
 */
@RequiredArgsConstructor
@Component
public class HttpServletFilter implements Filter, Ordered {

    private final ReqLogPrinter reqLogPrinter;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestIdUtil.setRequestIdIfAbsent((HttpServletRequest) request);

        String contentType = Optional.ofNullable(request.getContentType()).orElse("");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        boolean isPostJson = HttpMethod.POST.matches(httpServletRequest.getMethod())
                && contentType.toLowerCase().startsWith("application/json");
        if (!isPostJson) {
            // multipart/
            // application/x-www-form-urlencoded
            // ...
            reqLogPrinter.printRequest(httpServletRequest, "(...)");
        }

        doFilter(chain, request, response);
    }

    @Override
    public void destroy() {

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private void doFilter(FilterChain chain, ServletRequest request, ServletResponse response) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } finally {
            RequestIdUtil.clear();
        }
    }
}
