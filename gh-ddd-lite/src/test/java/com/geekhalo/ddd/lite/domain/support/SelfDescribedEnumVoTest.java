package com.geekhalo.ddd.lite.domain.support;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SelfDescribedEnumVoTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void apply() {
        SelfDescribedEnumVo t1Vo = SelfDescribedEnumVo.apply(TestEnum.T1);
        Assert.assertEquals("T1", t1Vo.getKey());
        Assert.assertEquals("t1", t1Vo.getDesc());

        SelfDescribedEnumVo t2Vo = SelfDescribedEnumVo.apply(TestEnum.T2);
        Assert.assertEquals("T2", t2Vo.getKey());
        Assert.assertEquals("t2", t2Vo.getDesc());
    }

    @Test
    public void apply1() {
        List<SelfDescribedEnumVo> vos = SelfDescribedEnumVo.apply(TestEnum.class);
        Assert.assertTrue(vos.contains(SelfDescribedEnumVo.apply(TestEnum.T1)));
        Assert.assertTrue(vos.contains(SelfDescribedEnumVo.apply(TestEnum.T2)));
    }

    enum TestEnum implements SelfDescribedEnum {
        NONE(""),T1("t1"), T2("t2");

        private final String descr;

        TestEnum(String descr) {
            this.descr = descr;
        }

        @Override
        public String getDescription() {
            return descr;
        }
    }
}