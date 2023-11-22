package com.digcoin.snapx.core.advice.log;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.InputStream;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/2 21:44
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
public class ReqLogHttpInputMessage implements HttpInputMessage {

    private InputStream body;

    private HttpHeaders headers;

    @Override
    public InputStream getBody() {
        return this.body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.headers;
    }

}
