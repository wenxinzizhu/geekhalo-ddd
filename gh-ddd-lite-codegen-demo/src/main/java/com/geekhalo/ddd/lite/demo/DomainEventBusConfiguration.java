package com.geekhalo.ddd.lite.demo;

import com.geekhalo.ddd.lite.domain.DomainEventBus;
import com.geekhalo.ddd.lite.domain.support.DefaultDomainEventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainEventBusConfiguration {
    @Bean
    public DomainEventBus domainEventBus(){
        return new DefaultDomainEventBus();
    }
}
