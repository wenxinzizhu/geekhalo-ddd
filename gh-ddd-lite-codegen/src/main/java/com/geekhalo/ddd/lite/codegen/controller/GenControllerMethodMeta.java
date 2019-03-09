package com.geekhalo.ddd.lite.codegen.controller;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.application.GenApplicationIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Map;

import static com.geekhalo.ddd.lite.codegen.utils.MethodUtils.getMethodKey;

@Value
public class GenControllerMethodMeta {
    @Getter(AccessLevel.PRIVATE)
    private final Map<String, MethodMeta> createMethodsMap = Maps.newHashMap();
    @Getter(AccessLevel.PRIVATE)
    private final Map<String, MethodMeta> updateMethodsMap = Maps.newHashMap();
    @Getter(AccessLevel.PRIVATE)
    private final Map<String, MethodMeta> queryMethodsMap = Maps.newHashMap();

    public void addCreateMethod(ExecutableElement method){
        String key = getMethodKey(method);
        createMethodsMap.computeIfAbsent(key, k->new MethodMeta(k))
                .addMethod(method);

    }

    public void addUpdateMethod(ExecutableElement method){
        String key = getMethodKey(method);
        updateMethodsMap.computeIfAbsent(key, k->new MethodMeta(k))
                .addMethod(method);
    }

    public void addQueryMethod(ExecutableElement method){
        String key = getMethodKey(method);
        queryMethodsMap.computeIfAbsent(key, k->new MethodMeta(k))
                .addMethod(method);
    }

    public List<MethodMeta> getCreateMethods() {
        return Lists.newArrayList(this.createMethodsMap.values());
    }

    public List<MethodMeta> getUpdateMethods() {
        return Lists.newArrayList(this.updateMethodsMap.values());
    }

    public List<MethodMeta> getQueryMethods() {
        return Lists.newArrayList(this.queryMethodsMap.values());
    }

    public class MethodMeta{
        private final String key;
        private final List<ExecutableElement> elements;

        MethodMeta(String key) {
            this.key = key;
            this.elements = Lists.newArrayList();
        }

        public void addMethod(ExecutableElement method){
            this.elements.add(method);
        }

        public boolean isIngnore(){
            return getExecutableElement().getAnnotation(GenApplicationIgnore.class) != null;
        }

        public ExecutableElement getExecutableElement(){
            return this.elements.get(0);
        }

        public String getMethodName() {
            return getExecutableElement().getSimpleName().toString();
        }

        public Description getDescription() {
            for (ExecutableElement executableElement : this.elements){
                Description description = executableElement.getAnnotation(Description.class);
                if (description != null){
                    return description;
                }
            }
            return null;
        }

        public TypeMirror getReturnType() {
            return getExecutableElement().getReturnType();
        }
    }
}
