package com.geekhalo.ddd.lite.codegen.application.repository;

import com.geekhalo.ddd.lite.codegen.application.GenApplicationIgnore;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.stream.Collectors;

public final class RepositoryBasedMethodMetaParser {

    public RepositoryBasedMethodMeta parse(TypeElement modelType, TypeElement modelVoType, TypeElement repositoryElement){
        RepositoryBasedMethodMeta methodMeta = new RepositoryBasedMethodMeta(modelType, modelVoType);
        ElementFilter.methodsIn(repositoryElement.getEnclosedElements()).stream()
                .filter(executableElement -> executableElement.getAnnotation(GenApplicationIgnore.class) == null)
//                .filter(executableElement -> executableElement.getAnnotation(QueryMethod.class) != null)
                .collect(Collectors.toSet())
                .forEach(executableElement -> methodMeta.addQueryMethod(executableElement));

        return methodMeta;
    }

}
