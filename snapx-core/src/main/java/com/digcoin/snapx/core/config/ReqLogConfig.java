package com.digcoin.snapx.core.config;

import com.digcoin.snapx.core.advice.log.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/3 09:56
 * @description
 */
@Import({
        HttpServletFilter.class,
        ReqLogPrinter.class,
        LogRequestBodyAdvice.class,
        LogResponseBodyAdvice.class,
})
@Configuration
public class ReqLogConfig {

}
