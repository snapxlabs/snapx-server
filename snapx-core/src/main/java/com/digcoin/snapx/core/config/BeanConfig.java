package com.digcoin.snapx.core.config;

import com.digcoin.snapx.core.advice.ServiceExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@EnableAspectJAutoProxy
public class BeanConfig {

    @Bean
    public ServiceExceptionHandler serviceExceptionHandler() {
        return new ServiceExceptionHandler();
    }

}
