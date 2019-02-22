package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.DomainEvent;
import com.geekhalo.ddd.lite.domain.DomainEventExecutor;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by taoli on 17/11/17.
 */
public class ExecutorBasedDomainEventExecutor implements DomainEventExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorBasedDomainEventExecutor.class);

    private final ExecutorService executorService;

    public ExecutorBasedDomainEventExecutor(String name, int nThreads, int buffer) {
        BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
                .namingPattern(name + "-%d")
                .daemon(true)
                .uncaughtExceptionHandler((t, e) -> {
                    LOGGER.error("failed to run task on {}.", t, e);
                })
                .build();

        this.executorService = new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(buffer),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public <E extends DomainEvent> void submit(Task<E> task) {
        executorService.submit(task);
    }
}
