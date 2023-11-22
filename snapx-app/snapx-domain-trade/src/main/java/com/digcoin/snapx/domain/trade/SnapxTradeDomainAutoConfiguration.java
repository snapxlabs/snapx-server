package com.digcoin.snapx.domain.trade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        "com.digcoin.snapx.domain.trade.config",
        "com.digcoin.snapx.domain.trade.component",
        "com.digcoin.snapx.domain.trade.service"
})
@MapperScan("com.digcoin.snapx.domain.trade.mapper")
public class SnapxTradeDomainAutoConfiguration {
}
