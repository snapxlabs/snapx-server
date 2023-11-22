package com.digcoin.snapx.core.web;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

public interface ApiAttachmentProvider {

    String getName();

    Object getAttachment(Object body,
                         MethodParameter returnType,
                         MediaType selectedContentType,
                         Class selectedConverterType,
                         ServerHttpRequest request,
                         ServerHttpResponse response);
}
