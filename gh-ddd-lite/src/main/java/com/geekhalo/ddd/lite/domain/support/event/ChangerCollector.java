package com.geekhalo.ddd.lite.domain.support.event;

import lombok.Value;

import java.util.List;

/**
 * Created by taoli on 17/11/23.
 */
public interface ChangerCollector {
    List<Item> collect(Object source);
    @Value
    class Item {
        private String name;
        private Object changer;
    }
}
