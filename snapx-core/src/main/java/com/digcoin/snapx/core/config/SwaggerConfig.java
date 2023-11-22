package com.digcoin.snapx.core.config;


import com.digcoin.snapx.core.advice.IgnoreApiBody;
import com.digcoin.snapx.core.config.properties.ApiDocInfo;
import com.digcoin.snapx.core.web.ApiBody;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springdoc.core.ReturnTypeParser;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

@EnableConfigurationProperties(ApiDocInfo.class)
@SecurityScheme(name = "authorization", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(ApiDocInfo apiDocInfo) {
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("authorization");
        return new OpenAPI()
                .info(new Info().title(apiDocInfo.getTitle())
                        .description(apiDocInfo.getDescription())
                        .version(apiDocInfo.getVersion()))
                .security(Arrays.asList(securityRequirement))
                .externalDocs(new ExternalDocumentation()
                        .description(apiDocInfo.getAppName())
                        .url(apiDocInfo.getAppUrl()));
    }

    @Bean
    public ReturnTypeParser customReturnTypeParser() {
        return new ReturnTypeParser() {
            @Override
            public Type getReturnType(MethodParameter methodParameter) {
                Type returnType = ReturnTypeParser.super.getReturnType(methodParameter);
                Annotation[] annotations =
                        Objects.requireNonNull(methodParameter.getMethod())
                                .getDeclaringClass()
                                .getAnnotations();
                if (Arrays.stream(annotations)
                        .noneMatch(annotation -> annotation instanceof RestController)) {
                    return returnType;
                }

                if (Arrays.stream(Objects.requireNonNull(methodParameter.getMethod())
                        .getAnnotations()).anyMatch(annotation -> annotation instanceof IgnoreApiBody)) {
                    return returnType;
                }

                if (returnType instanceof ParameterizedType) {
                    ParameterizedType actualType = (ParameterizedType) returnType;
                    if (Objects.equals(actualType.getRawType(), ApiBody.class)) {
                        return returnType;
                    }
                }

                if (Objects.equals(returnType, Void.TYPE)) {
                    return ApiBody.class;
                }

                return TypeUtils.parameterize(ApiBody.class, returnType);
            }
        };
    }

}
