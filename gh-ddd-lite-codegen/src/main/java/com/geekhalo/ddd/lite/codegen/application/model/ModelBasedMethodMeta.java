package com.geekhalo.ddd.lite.codegen.application.model;

import com.google.common.collect.Lists;
import lombok.Value;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;

@Value
public final class ModelBasedMethodMeta {
    private final TypeElement modelType;
    private final List<ExecutableElement> updateMethods = Lists.newArrayList();
    private final List<ExecutableElement> createMethods = Lists.newArrayList();

    public void addUpdateMethod(ExecutableElement element){
        this.updateMethods.add(element);
    }

    public void addCreateMethod(ExecutableElement element){
        this.createMethods.add(element);
    }

}
