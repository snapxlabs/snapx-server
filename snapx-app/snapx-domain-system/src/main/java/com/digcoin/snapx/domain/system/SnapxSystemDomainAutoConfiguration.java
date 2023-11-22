package com.digcoin.snapx.domain.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.digcoin.snapx.domain.system.service")
@MapperScan("com.digcoin.snapx.domain.system.mapper")
public class SnapxSystemDomainAutoConfiguration {
}
