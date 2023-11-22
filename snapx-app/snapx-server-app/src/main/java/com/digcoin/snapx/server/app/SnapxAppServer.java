package com.digcoin.snapx.server.app;

import com.digcoin.snapx.core.config.*;
import com.digcoin.snapx.server.app.member.config.MemberExPassProperties;
import com.digcoin.snapx.server.app.member.config.MemberScheduleProperties;
import com.digcoin.snapx.server.base.system.config.QrcodeProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import({
        ApiConfig.class,
        AuthConfig.class,
        BeanConfig.class,
        JacksonConfig.class,
        MybatisConfig.class,
        SwaggerConfig.class,
        MemberExPassProperties.class,
        MemberScheduleProperties.class,
        ReqLogConfig.class
})
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties({QrcodeProperties.class})
@SpringBootApplication
public class SnapxAppServer {

    public static void main(String[] args) {
        SpringApplication.run(SnapxAppServer.class, args);
    }

}
