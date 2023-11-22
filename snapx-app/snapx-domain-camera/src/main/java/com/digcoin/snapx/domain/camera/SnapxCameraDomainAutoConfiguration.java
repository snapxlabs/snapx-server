package com.digcoin.snapx.domain.camera;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/9 10:33
 * @description
 */
@Configuration
@ComponentScan("com.digcoin.snapx.domain.camera.config")
@ComponentScan("com.digcoin.snapx.domain.camera.component")
@ComponentScan("com.digcoin.snapx.domain.camera.service")
@MapperScan("com.digcoin.snapx.domain.camera.mapper")
public class SnapxCameraDomainAutoConfiguration {
}
