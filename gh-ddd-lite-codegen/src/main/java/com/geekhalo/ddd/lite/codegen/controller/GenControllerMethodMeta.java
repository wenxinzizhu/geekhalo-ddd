package com.geekhalo.ddd.lite.codegen.controller;

import com.google.common.collect.Lists;
import lombok.Value;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

@Value
public class GenControllerMethodMeta {
    private final List<ExecutableElement> createMethods = Lists.newArrayList();
    private final List<ExecutableElement> updateMethods = Lists.newArrayList();
    private final List<ExecutableElement> queryMethods = Lists.newArrayList();

    public void addCreateMethod(ExecutableElement method){
        this.createMethods.add(method);
    }

    public void addUpdateMethod(ExecutableElement method){
        this.updateMethods.add(method);
    }

    public void addQueryMethod(ExecutableElement method){
        this.queryMethods.add(method);
    }
}
