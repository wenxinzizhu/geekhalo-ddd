package com.geekhalo.ddd.lite.codegen.controller.request;

import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

public interface RequestBodyInfo{
        String getParameterName();
        TypeName getParameterType();
        List<String> getCallParams();

        List<JavaSource> getSubType();
}