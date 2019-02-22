package com.geekhalo.ddd.lite.codegen;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

public final class JavaSourceCollector {
    private final Map<String, JavaSource> javaSources = Maps.newHashMap();

    public Collection<JavaSource> getAllJavaSource(){
        return this.javaSources.values();
    }
    public JavaSource getByName(String name){
        return javaSources.get(name);
    }

    public void register(String name, JavaSource javaSource){
        this.javaSources.put(name, javaSource);
    }

    public void register(JavaSource javaSource){
        register(javaSource.getFullName(), javaSource);
    }
}
