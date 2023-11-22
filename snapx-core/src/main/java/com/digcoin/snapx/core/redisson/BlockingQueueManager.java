package com.digcoin.snapx.core.redisson;

import com.digcoin.snapx.core.error.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonShutdownException;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class BlockingQueueManager<T> implements ApplicationEventPublisherAware {
    private final String name;
    private ApplicationEventPublisher eventPublisher;
    private final RedissonClient redissonClient;
    private final Codec codec;
    private final Boolean listen;
    private ExecutorService executorService;

    private RBlockingQueue<T> blockingQueue;

    public BlockingQueueManager(String name, RedissonClient redissonClient, Codec codec, Boolean listen) {
        this.name = name;
        this.redissonClient = redissonClient;
        this.codec = codec;
        this.listen = listen;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void init() {
        blockingQueue = redissonClient.getBlockingQueue(name, codec);
        if (listen) {
            this.executorService = Executors.newSingleThreadExecutor(runnable -> {
                Thread thread = new Thread(runnable);
                thread.setName(name);
                thread.setDaemon(true);
                return thread;
            });

            this.executorService.submit(() -> {
                while (!this.executorService.isShutdown() && !this.executorService.isTerminated()) {
                    try {
                        T event = this.blockingQueue.take();
                        this.eventPublisher.publishEvent(event);

                    } catch (RedissonShutdownException e) {
                        if (!this.executorService.isShutdown() && !this.executorService.isTerminated()) {
                            log.error("blocking-queue-manager [{}] redisson is shutdown", this.name, e);
                        }

                    } catch (InterruptedException e) {
                        log.error("blocking-queue-manager [{}] redisson is interrupted", this.name, e);

                    } catch (BusinessException e) {
                        log.error("blocking-queue-manager [{}] business exception occur", this.name, e);

                    } catch (Exception e) {
                        log.error("blocking-queue-manager [{}] unexpect exception occur", this.name, e);
                    }
                }
            });
        }
    }

    public void destroy() {
        if(Objects.nonNull(this.executorService)) {
            this.executorService.shutdown();
        }
    }

    public void publishEvent(T event) {
        Objects.requireNonNull(event);
        this.blockingQueue.putAsync(event);
    }

}
