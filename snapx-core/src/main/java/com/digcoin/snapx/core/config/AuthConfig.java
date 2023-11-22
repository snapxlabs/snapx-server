package com.digcoin.snapx.core.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import com.digcoin.snapx.core.advice.CurrentUserHandler;
import com.digcoin.snapx.core.web.CurrentUserProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserHandler());
    }

    @Bean
    public CurrentUserProvider currentUserProvider() {
        return new CurrentUserProvider();
    }

    @Bean
    public CurrentUserHandler currentUserHandler() {
        return new CurrentUserHandler(currentUserProvider());
    }

}
