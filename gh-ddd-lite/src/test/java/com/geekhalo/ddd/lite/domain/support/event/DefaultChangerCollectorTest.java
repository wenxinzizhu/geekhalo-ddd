package com.geekhalo.ddd.lite.domain.support.event;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by taoli on 17/11/23.
 */
public class DefaultChangerCollectorTest {
    private Data data;
    private ChangerCollector changerCollector;

    @Before
    public void setUp() throws Exception {
        this.data = new Data();
        this.data.intChanger = IntChanger.apply(1, 2);
        this.data.integerChanger = IntegerChanger.apply(1,2);
        this.data.longChanger = LongChanger.apply(1L, 2L);
        this.data.objectChanger = ObjectChanger.accept(1L, 4);
        this.data.stringChanger = StringChanger.apply("112", "2321");

        this.changerCollector =  DefaultChangerCollector.getInstance();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void collect() throws Exception {
        List<ChangerCollector.Item> items = this.changerCollector.collect(this.data);
        Assert.assertEquals(5, items.size());
    }

    class Data {
        private IntChanger intChanger;
        private IntegerChanger integerChanger;
        private LongChanger longChanger;
        private ObjectChanger objectChanger;
        private StringChanger stringChanger;
    }

}