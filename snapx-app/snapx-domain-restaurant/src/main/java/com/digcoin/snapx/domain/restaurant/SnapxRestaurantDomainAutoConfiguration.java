package com.digcoin.snapx.domain.restaurant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:33
 * @description
 */
@Configuration
@ComponentScan("com.digcoin.snapx.domain.restaurant.config")
@ComponentScan("com.digcoin.snapx.domain.restaurant.component")
@ComponentScan("com.digcoin.snapx.domain.restaurant.service")
@MapperScan("com.digcoin.snapx.domain.restaurant.mapper")
public class SnapxRestaurantDomainAutoConfiguration {
}
