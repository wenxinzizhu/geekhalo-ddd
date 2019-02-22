package com.geekhalo.ddd.lite.codegen.support;

import com.squareup.javapoet.TypeSpec;

public interface MethodWriter {
    void writeTo(TypeSpec.Builder builder);
}
