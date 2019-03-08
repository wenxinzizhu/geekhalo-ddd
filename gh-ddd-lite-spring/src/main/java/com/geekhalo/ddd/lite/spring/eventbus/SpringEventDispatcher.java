package com.geekhalo.ddd.lite.spring.eventbus;

import com.geekhalo.ddd.lite.domain.DomainEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import javax.annotation.PostConstruct;

class SpringEventDispatcher implements ApplicationEventPublisherAware {

    @Autowired
    private DomainEventBus domainEventBus;

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    public void addListener(){
        this.domainEventBus.register(event->true, event -> {this.eventPublisher.publishEvent(event);});
    }

}
