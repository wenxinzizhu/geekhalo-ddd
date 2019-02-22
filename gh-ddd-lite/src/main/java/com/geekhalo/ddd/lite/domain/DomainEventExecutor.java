package com.geekhalo.ddd.lite.domain;

import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by taoli on 17/11/16.
 */
public interface DomainEventExecutor {
    Logger LOGGER = LoggerFactory.getLogger(DomainEventExecutor.class);

    default <E extends DomainEvent>  void submit(DomainEventHandler<E> handler, E event){
        submit(new Task<>(handler, event));
    }

    <E extends DomainEvent> void submit(Task<E> task);

    @Value
    class Task<E extends DomainEvent> implements Runnable{
        private final DomainEventHandler<E> handler;
        private final E event;

        @Override
        public void run() {
            try {
                this.handler.handle(this.event);
            }catch (Exception e){
                LOGGER.error("failed to handle event {} use {}", this.event, this.handler, e);
            }

        }
    }

    class SyncExecutor implements DomainEventExecutor{
        @Override
        public <E extends DomainEvent> void submit(Task<E> task) {
            task.run();
        }
    }
}
