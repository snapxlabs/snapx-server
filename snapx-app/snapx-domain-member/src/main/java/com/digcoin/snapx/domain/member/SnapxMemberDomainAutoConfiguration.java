package com.digcoin.snapx.domain.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.digcoin.snapx.domain.member.config")
@ComponentScan("com.digcoin.snapx.domain.member.component")
@ComponentScan("com.digcoin.snapx.domain.member.service")
@MapperScan("com.digcoin.snapx.domain.member.mapper")
public class SnapxMemberDomainAutoConfiguration {
}
