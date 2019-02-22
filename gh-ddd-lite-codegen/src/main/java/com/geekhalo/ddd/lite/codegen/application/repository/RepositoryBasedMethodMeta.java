package com.geekhalo.ddd.lite.codegen.application.repository;

import com.google.common.collect.Lists;
import lombok.Data;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;

@Data
public final class RepositoryBasedMethodMeta {
    private final TypeElement modelType;
    private final TypeElement modelVoType;
    private final List<ExecutableElement> queryMethods = Lists.newArrayList();

    public RepositoryBasedMethodMeta(TypeElement modelType, TypeElement modelVoType) {
        this.modelType = modelType;
        this.modelVoType = modelVoType;
    }

    public void addQueryMethod(ExecutableElement executableElement){
        this.queryMethods.add(executableElement);
    }
}
