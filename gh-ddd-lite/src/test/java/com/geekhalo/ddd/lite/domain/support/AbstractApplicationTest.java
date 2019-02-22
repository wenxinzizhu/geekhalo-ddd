package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.DomainEvent;
import com.geekhalo.ddd.lite.domain.DomainEventHandler;
import com.geekhalo.ddd.lite.domain.ValidationHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by taoli on 17/11/17.
 */
public class AbstractApplicationTest {
    private TestApplication testApplication;
    private DefaultDomainEventBus defaultDomainEventBus = new DefaultDomainEventBus();
    private TestDomainEventHandler eventHandler;
    @Before
    public void setUp() throws Exception {
        this.testApplication = new TestApplication();
        this.eventHandler = new TestDomainEventHandler();
        defaultDomainEventBus.register(NameUpdatedEvent.class, eventHandler);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void updateName() throws Exception{
        this.testApplication.create();
        this.testApplication.update(1l, testAgg -> testAgg.updateName("name"));
        Assert.assertFalse(this.eventHandler.events.isEmpty());
    }

    @Test
    public void buildCreator() throws Exception {
        TestAgg testAgg = this.testApplication.create();
        Assert.assertNotNull(testAgg);
    }

    @Test
    public void buildUpdater() throws Exception {
        TestAgg testAgg = this.testApplication.create();
        Assert.assertNotNull(testAgg);
        TestAgg testAgg1 = this.testApplication.update();
        Assert.assertNotNull(testAgg1);
    }

    @Test
    public void buildSyncer() throws Exception{
        TestAgg testAgg = this.testApplication.sync();
        Assert.assertNotNull(testAgg);
    }

    private class TestDomainEventHandler implements DomainEventHandler {
        private List<DomainEvent> events = Lists.newArrayList();

        @Override
        public void handle(DomainEvent event) {
            this.events.add(event);
        }
    }

    private class TestApplication extends AbstractApplication {
        private final TestRepository testRepository = new TestRepository();

        protected TestApplication() {
            super(LoggerFactory.getLogger(TestApplication.class));
        }

        public TestAgg create(){
            return creatorFor(testRepository)
                    .publishBy(defaultDomainEventBus)
                    .instance(()->new TestAgg())
                    .update(testAgg -> testAgg.setName("123"))
                    .call();
        }

        public TestAgg update(Long id, Consumer<TestAgg> updater){
            return updaterFor(this.testRepository)
                    .publishBy(defaultDomainEventBus)
                    .id(id)
                    .update(updater)
                    .call();
        }

        public TestAgg update(){
            return updaterFor(this.testRepository)
                    .publishBy(defaultDomainEventBus)
                    .id(1l)
                    .update(testAgg -> testAgg.setName("456"))
                    .call();
        }

        public TestAgg sync(){
            return syncerFor(this.testRepository)
                    .publishBy(defaultDomainEventBus)
                    .loadBy(() -> testRepository.getById(100l))
                    .instance(()->new TestAgg())
                    .update(testAgg -> testAgg.setName("10000"))
                    .call();
        }
    }

    private class TestRepository extends AbstractRepository<Long, TestAgg> {
        private final AtomicLong idGen = new AtomicLong(1);
        private final Map<Long, TestAgg> data = Maps.newHashMap();

        @Override
        public Optional<TestAgg> getById(Long aLong) {
            return Optional.ofNullable(data.get(aLong));
        }

        @Override
        public List<TestAgg> getByIds(List<Long> longs) {
            return longs.stream()
                    .map(id->getById(id))
                    .map(op-> op.orElse(null))
                    .filter(agg->agg != null)
                    .collect(Collectors.toList());
        }

        @Override
        public void save(TestAgg testAgg) {
            testAgg.id = idGen.getAndAdd(1);
            this.data.put(testAgg.id, testAgg);
        }

        @Override
        public void update(TestAgg testAgg) {
            data.put(testAgg.id, testAgg);
        }

        @Override
        public void delete(TestAgg testAgg) {
            data.remove(testAgg.id);
        }
    }

    @Data
    private class TestAgg extends AbstractAggregate<Long> {
        private Long id;
        private String name;

        private TestAgg(){

        }

        public void updateName(String name){
            String oName = getName();
            setName(name);

            registerEvent(new NameUpdatedEvent(this, oName, name));

        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public int getVersion() {
            return 0;
        }

        @Override
        public void validate(ValidationHandler handler) {
            if (StringUtils.isEmpty(name)){
                handler.handleError("name can not be null");
            }
        }
    }

    class NameUpdatedEvent extends AbstractDomainEvent<Long, TestAgg> implements DomainEvent<Long, TestAgg> {
        private final String oldValue;
        private final String newValue;

        public NameUpdatedEvent(TestAgg source, String oldValue, String newValue) {
            super(source);
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

    }

}