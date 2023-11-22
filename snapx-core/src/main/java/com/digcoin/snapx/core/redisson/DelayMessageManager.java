package com.digcoin.snapx.core.redisson;

import com.digcoin.snapx.core.error.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonShutdownException;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
public class DelayMessageManager<T extends DelayMessage> implements ApplicationEventPublisherAware {

    private final String name;

    private final Function<Long, T> eventBuilder;

    private final RedissonClient redissonClient;
    private final Boolean listen;

    private RBlockingQueue<Long> destQueue;
    private RDelayedQueue<Long> delayedQueue;
    private ExecutorService executorService;

    private ApplicationEventPublisher eventPublisher;

    public DelayMessageManager(String name, Function<Long, T> eventBuilder, RedissonClient redissonClient, Boolean listen) {
        this.name = name;
        this.eventBuilder = eventBuilder;
        this.redissonClient = redissonClient;
        this.listen = listen;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void init() {
        this.destQueue = this.redissonClient.getBlockingQueue(this.name);
        this.delayedQueue = this.redissonClient.getDelayedQueue(this.destQueue);

        if (this.listen) {
            this.executorService = Executors.newSingleThreadExecutor(runnable -> {
                Thread thread = new Thread(runnable);
                thread.setName("redisson-delay-task-" + name);
                thread.setDaemon(true);
                return thread;
            });

            this.executorService.submit(() -> {
                while (!this.executorService.isShutdown() && !this.executorService.isTerminated()) {
                    try {
                        Long id = this.destQueue.take();
                        this.eventPublisher.publishEvent(eventBuilder.apply(id));

                    } catch (RedissonShutdownException e) {
                        if (!this.executorService.isShutdown() && !this.executorService.isTerminated()) {
                            log.error("delay-message-manager [{}] redisson is shutdown", this.name, e);
                        }
                        break;

                    } catch (InterruptedException e) {
                        log.error("delay-message-manager [{}] redisson is interrupted", this.name, e);

                    } catch (BusinessException e) {
                        log.error("delay-message-manager [{}] business exception occur", this.name, e);

                    } catch (Exception e) {
                        log.error("delay-message-manager [{}] unexpect exception occur", this.name, e);
                    }
                }
            });
        }
    }

    public void destroy() {
        if (Objects.nonNull(this.delayedQueue)) {
            this.delayedQueue.destroy();
        }

        if(Objects.nonNull(this.executorService)) {
            this.executorService.shutdown();
        }
    }

    public void publishEvent(T event, Long delay, TimeUnit timeUnit) {
        Objects.requireNonNull(event);
        this.delayedQueue.offer(event.getId(), delay, timeUnit);
    }

    public void revokeEvent(T event) {
        Objects.requireNonNull(event);
        this.delayedQueue.remove(event.getId());
    }

}
