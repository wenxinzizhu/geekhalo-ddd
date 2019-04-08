package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.DomainEventBus;
import com.geekhalo.ddd.lite.domain.DomainEventHandlerRegistry;
import com.geekhalo.ddd.lite.domain.DomainEventPublisher;

public class DomainEventBusHolder {
    private static final ThreadLocal<DomainEventBus> THREAD_LOCAL = new ThreadLocal<DomainEventBus>(){
        @Override
        protected DomainEventBus initialValue() {
            return new DefaultDomainEventBus();
        }
    };

    public static DomainEventPublisher getPubliser(){
        return THREAD_LOCAL.get();
    }

    public static DomainEventHandlerRegistry getHandlerRegistry(){
        return THREAD_LOCAL.get();
    }

    public static void clean(){
        THREAD_LOCAL.remove();
    }
}
