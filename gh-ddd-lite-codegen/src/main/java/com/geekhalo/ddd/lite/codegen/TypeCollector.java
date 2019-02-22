package com.geekhalo.ddd.lite.codegen;

import com.google.common.collect.Maps;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Map;

public final class TypeCollector {
    private final Map<String, TypeElement> typeElements = Maps.newHashMap();

    public void syncForm(RoundEnvironment roundEnv){
        for (Element element :  roundEnv.getRootElements()){
            if (element instanceof TypeElement){
                String name = ((TypeElement) element).getQualifiedName().toString();
                this.typeElements.put(name, (TypeElement) element);
            }
        }
    }

    public TypeElement getByName(String name){
        return this.typeElements.get(name);
    }
}
