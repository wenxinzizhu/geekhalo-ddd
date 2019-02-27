package com.geekhalo.ddd.lite.domain.support.vo;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MoneyTest {

    @Test
    public void add() {
        Money m1 = Money.apply(100L);
        Money m2 = Money.apply(200L);
        Money money = m1.add(m2);
        Assert.assertEquals(300L, money.getTotalFee().longValue());
        Assert.assertEquals(m1.getFeeType(), money.getFeeType());
        Assert.assertEquals(m2.getFeeType(), money.getFeeType());
    }

    @Test
    public void subtract() {
        Money m1 = Money.apply(300L);
        Money m2 = Money.apply(200L);
        Money money = m1.subtract(m2);
        Assert.assertEquals(100L, money.getTotalFee().longValue());
        Assert.assertEquals(m1.getFeeType(), money.getFeeType());
        Assert.assertEquals(m2.getFeeType(), money.getFeeType());
    }

    @Test
    public void multiply() {
        Money m1 = Money.apply(100L);
        Money money = m1.multiply(3);
        Assert.assertEquals(300L, money.getTotalFee().longValue());
        Assert.assertEquals(m1.getFeeType(), money.getFeeType());
    }

    @Test
    public void split() {
        Money m1 = Money.apply(100L);
        List<Money> monies = m1.split(33);
        Assert.assertEquals(33, monies.size());
        monies.forEach(m -> Assert.assertEquals(m1.getFeeType(), m.getFeeType()));
        long total = monies.stream()
                .mapToLong(m->m.getTotalFee())
                .sum();
        Assert.assertEquals(100L, total);
    }
}