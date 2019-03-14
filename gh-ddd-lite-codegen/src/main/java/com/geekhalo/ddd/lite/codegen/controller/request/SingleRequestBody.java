package com.geekhalo.ddd.lite.codegen.controller.request;

import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SingleRequestBody implements RequestBodyInfo {
    private final TypeName typeName;
    private final String name;

    SingleRequestBody(VariableElement param) {
        this.typeName = TypeName.get(param.asType());
        this.name = param.getSimpleName().toString();
    }

    public SingleRequestBody(ClassName className, String name) {
        this.typeName = className;
        this.name = name;
    }

    @Override
    public String getParameterName() {
        return this.name;
    }

    @Override
    public TypeName getParameterType() {
        return this.typeName;
    }

    @Override
    public List<String> getCallParams() {
        return Arrays.asList(name);
    }

    @Override
    public List<JavaSource> getSubType() {
        return Collections.emptyList();
    }
}