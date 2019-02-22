package com.geekhalo.ddd.lite.codegen.creator.support.writer;

import com.geekhalo.ddd.lite.codegen.creator.support.meta.CreatorSetterMeta;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

public interface CreatorWriter {
    void writeTo(TypeSpec.Builder builder, MethodSpec.Builder acceptMethodBuilder, List<CreatorSetterMeta> setterMetas);
}
