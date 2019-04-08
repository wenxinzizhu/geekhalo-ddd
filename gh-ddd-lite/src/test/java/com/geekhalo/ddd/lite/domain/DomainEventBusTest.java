package com.geekhalo.ddd.lite.domain;

import com.geekhalo.ddd.lite.domain.support.AbstractDomainEvent;
import com.geekhalo.ddd.lite.domain.support.DefaultDomainEventBus;
import lombok.Value;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DomainEventBusTest {
    private DomainEventBus domainEventBus;

    @Before
    public void setUp() throws Exception {
        this.domainEventBus = new DefaultDomainEventBus();
    }

    @After
    public void tearDown() throws Exception {
        this.domainEventBus = null;
    }

    @Test
    public void publishTest(){
        // 创建事件处理器
        TestEventHandler eventHandler = new TestEventHandler();
        // 注册事件处理器
        this.domainEventBus.register(TestEvent.class, eventHandler);

        // 发布事件
        this.domainEventBus.publish(new TestEvent("123"));

        // 检测事件处理器是够运行
        Assert.assertEquals("123", eventHandler.data);
    }

    @Value
    class TestEvent extends AbstractDomainEvent{
        private String data;
    }

    class TestEventHandler implements DomainEventHandler<TestEvent>{
        private String data;
        @Override
        public void handle(TestEvent event) {
            this.data = event.getData();
        }
    }
}