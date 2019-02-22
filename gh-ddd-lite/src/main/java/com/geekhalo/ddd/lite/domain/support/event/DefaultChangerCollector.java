package com.geekhalo.ddd.lite.domain.support.event;

import com.google.common.collect.Lists;
import org.springframework.util.ReflectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by taoli on 17/11/23.
 */
public class DefaultChangerCollector implements ChangerCollector{
    private static final DefaultChangerCollector INSTANCE = new DefaultChangerCollector();
    private DefaultChangerCollector(){

    }

    public static DefaultChangerCollector getInstance(){
        return INSTANCE;
    }

    @Override
    public List<Item> collect(Object source) {
        if (source == null){
            return Collections.emptyList();
        }
        List<Item> result = Lists.newArrayList();
        ReflectionUtils.doWithFields(source.getClass(), field -> {
            Class fieldCls = field.getType();
            if (Changer.class.isAssignableFrom(fieldCls)){
                field.setAccessible(true);
                Object value = field.get(source);
                if (value != null){
                    result.add(new Item(field.getName(), value));
                }
            }
        });

        return result;
    }
}
