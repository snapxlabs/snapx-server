package com.digcoin.snapx.core.config;

import com.digcoin.snapx.core.advice.GlobalExceptionHandler;
import com.digcoin.snapx.core.advice.ApiAttachmentWrapping;
import com.digcoin.snapx.core.advice.ApiBodyWrapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Value("${app.api.body-wrapping.package:com.digcoin.snapx.server}")
    private String ApiBodyWrappingPackage;

    @Bean
    public ApiBodyWrapping responseBodyWrapping() {
        return new ApiBodyWrapping(ApiBodyWrappingPackage);
    }

    @Bean
    public ApiAttachmentWrapping responseAttachmentWrapping() {
        return new ApiAttachmentWrapping();
    }

    @Bean
    public GlobalExceptionHandler baseGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
