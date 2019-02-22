package com.geekhalo.ddd.lite.codegen.dto.support.writer;

import com.geekhalo.ddd.lite.codegen.dto.support.meta.DtoGetterMeta;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.List;


public interface DtoMethodWriter {
    void write(TypeSpec.Builder builder,
               MethodSpec.Builder cMethodSpecBuilder,
               List<DtoGetterMeta> methodMetas);
}
