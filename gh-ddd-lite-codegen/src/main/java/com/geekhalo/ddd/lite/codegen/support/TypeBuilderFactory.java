package com.geekhalo.ddd.lite.codegen.support;

import com.squareup.javapoet.TypeSpec;

public interface TypeBuilderFactory {
    TypeSpec.Builder create();
}
