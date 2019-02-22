package com.geekhalo.ddd.lite.codegen;

import com.squareup.javapoet.TypeSpec;
import lombok.Value;

@Value
public class JavaSource{
    private final String pkgName;
    private final String clsName;
    private final TypeSpec.Builder typeSpecBuilder;

    public String getFullName() {
        return pkgName + "." +clsName;
    }
}