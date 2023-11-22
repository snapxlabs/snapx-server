package com.digcoin.snapx.server.admin;

import com.digcoin.snapx.core.config.*;
import com.digcoin.snapx.server.base.system.config.QrcodeProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Import({
        ApiConfig.class,
        AuthConfig.class,
        BeanConfig.class,
        JacksonConfig.class,
        MybatisConfig.class,
        SwaggerConfig.class,
        ReqLogConfig.class
})
@EnableConfigurationProperties({QrcodeProperties.class})
@SpringBootApplication
public class SnapxAdminServer {

    public static void main(String[] args) {
        SpringApplication.run(SnapxAdminServer.class, args);
    }

}
