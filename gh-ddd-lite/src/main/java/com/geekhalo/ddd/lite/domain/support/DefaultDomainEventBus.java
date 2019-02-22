package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.*;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Created by taoli on 17/11/17.
 */
public class DefaultDomainEventBus implements DomainEventBus {
    private final List<RegisterItem> registerItems = Lists.newCopyOnWriteArrayList();

    @Override
    public <ID, EVENT extends DomainEvent> void publish(EVENT event) {
        registerItems.forEach(registerItem -> registerItem.handEvent(event));
    }

    @Override
    public <E extends DomainEvent> void register(DomainEventSubscriber<E> subscriber, DomainEventExecutor executor, DomainEventHandler<E> handler) {
        registerItems.add(new RegisterItem(subscriber, executor, handler));
    }

    @Override
    public <E extends DomainEvent> void unregister(DomainEventSubscriber<E> subscriber) {
        doUnregister(registerItem -> registerItem.getSubscriber() == subscriber);
    }

    @Override
    public <E extends DomainEvent> void unregisterAll(DomainEventHandler<E> handler) {
        doUnregister(registerItem -> registerItem.getEventHandler() == handler);
    }

    private void doUnregister(Function<RegisterItem, Boolean> checker){
        Iterator<RegisterItem> registerItemIterator = this.registerItems.iterator();
        while (registerItemIterator.hasNext()){
            RegisterItem registerItem = registerItemIterator.next();
            if (checker.apply(registerItem)){
                registerItemIterator.remove();
            }
        }
    }

    @Data
    private class RegisterItem{
        private final DomainEventSubscriber subscriber;
        private final DomainEventExecutor executor;
        private final DomainEventHandler eventHandler;

        public <EVENT extends DomainEvent> void handEvent(EVENT event) {
            if (subscriber.accept(event)){
                executor.submit(eventHandler, event);
            }
        }
    }
}
