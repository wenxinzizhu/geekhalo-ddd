package com.geekhalo.ddd.lite.codegen.controller.request;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.List;
import java.util.stream.Collectors;

public class CreatedRequestBody implements RequestBodyInfo {
    private final String parameterName = "req";
    private final String parameterType;
    private final List<String> callParams;

    CreatedRequestBody(String parameterType, List<String> callParams) {
        this.parameterType = parameterType;
        this.callParams = callParams.stream()
                .map(p-> p.substring(0,1).toUpperCase() + p.substring(1))
                .map(p-> parameterName + ".get" + p + "()")
                .collect(Collectors.toList());
    }

    @Override
    public String getParameterName() {
        return parameterName;
    }

    @Override
    public TypeName getParameterType() {
        return ClassName.bestGuess(parameterType);
    }

    @Override
    public List<String> getCallParams() {
        return callParams;
    }
}