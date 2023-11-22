package com.digcoin.snapx.domain.infra.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ResourceLocationConfig implements WebMvcConfigurer {

    private final ResourceFileProperties resourceFileProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourceFileProperties.getPath() + "/**")
                .addResourceLocations("file:"+resourceFileProperties.getLocation() + "/");
    }
}
