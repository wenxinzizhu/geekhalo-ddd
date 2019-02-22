package com.geekhalo.ddd.lite.spring.eventbus;

import com.geekhalo.ddd.lite.domain.DomainEventBus;
import com.geekhalo.ddd.lite.domain.support.DefaultDomainEventBus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBusAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DomainEventBus domainEventBus(){
        return new DefaultDomainEventBus();
    }
}
