package com.digcoin.snapx.server.app.restaurant.config;

import com.digcoin.snapx.domain.restaurant.constant.ExecutorNameConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/20 13:31
 * @description
 */
@Configuration
public class StorageRstPhotoExecutorConfiguration {

    @Bean(name = ExecutorNameConst.STORAGE_RST_PHOTO_EXECUTOR)
    public ThreadPoolTaskExecutor storageRstPhotoExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix(String.format("Thread-%s-", ExecutorNameConst.STORAGE_RST_PHOTO_EXECUTOR));
        threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(1000);
        threadPoolTaskExecutor.afterPropertiesSet();
        return threadPoolTaskExecutor;
    }

}
