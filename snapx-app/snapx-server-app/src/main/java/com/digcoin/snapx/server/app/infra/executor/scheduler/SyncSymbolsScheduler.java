package com.digcoin.snapx.server.app.infra.executor.scheduler;

import com.digcoin.snapx.server.base.infra.service.BaseExchangeRatesService;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/5/10 12:05
 * @description
 */
@Slf4j
@AllArgsConstructor
@Component
public class SyncSymbolsScheduler {

    private final BaseExchangeRatesService baseExchangeRatesService;
    private final RedissonClient redissonClient;

    @Async
    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.HOURS)
    public void run() {
        RLock lock = redissonClient.getLock(Joiner.on(":").join(SyncSymbolsScheduler.class, "run"));
        if (!lock.tryLock()) {
            return;
        }
        try {
            baseExchangeRatesService.syncLatest();
        } finally {
            lock.unlock();
        }
    }

}