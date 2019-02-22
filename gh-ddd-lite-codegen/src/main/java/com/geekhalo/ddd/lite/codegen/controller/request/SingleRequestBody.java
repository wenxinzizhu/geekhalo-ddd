package com.geekhalo.ddd.lite.codegen.controller.request;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.List;

public class SingleRequestBody implements RequestBodyInfo {
    private final VariableElement param;

    SingleRequestBody(VariableElement param) {
        this.param = param;
    }

    @Override
    public String getParameterName() {
        return param.getSimpleName().toString();
    }

    @Override
    public TypeName getParameterType() {
        return TypeName.get(param.asType());
    }

    @Override
    public List<String> getCallParams() {
        return Arrays.asList(param.getSimpleName().toString());
    }
}