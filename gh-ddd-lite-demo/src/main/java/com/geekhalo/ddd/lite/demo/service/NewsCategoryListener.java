package com.geekhalo.ddd.lite.demo.service;

import com.geekhalo.ddd.lite.demo.domain.news.category.NewsCategoryCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NewsCategoryListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsCategoryListener.class);

    @EventListener
    public void handle(NewsCategoryCreatedEvent event){
        LOGGER.info("handle {}", event);
    }
}
