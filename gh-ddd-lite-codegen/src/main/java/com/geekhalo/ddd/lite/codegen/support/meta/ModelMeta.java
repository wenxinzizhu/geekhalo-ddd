package com.geekhalo.ddd.lite.codegen.support.meta;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ModelMeta<M extends ModelMethodMeta> {
    private final TypeElement typeElement;
    private final Map<String, M> methods = Maps.newHashMap();

    public ModelMeta(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public void addMethodMeta(M m){
        M meta = this.methods.get(m.name());
        if (meta != null){
            merge(meta, m);
        }else {
            this.methods.put(m.name(), m);
        }
    }

    protected void merge(M s, M n){

    }

    public List<M> getMethodMetas(){
        return Lists.newArrayList(this.methods.values().stream().filter(m -> !m.ignore()).collect(Collectors.toList()));
    }
}
