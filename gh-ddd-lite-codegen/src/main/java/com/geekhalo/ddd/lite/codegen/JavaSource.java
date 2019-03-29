package com.geekhalo.ddd.lite.codegen;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
public class JavaSource{
    private final String pkgName;
    private final String clsName;
    @Getter(AccessLevel.PRIVATE)
    private final TypeSpec.Builder typeSpecBuilder;

    public String getFullName() {
        return pkgName + "." +clsName;
    }

    public TypeSpec getTypeSpec(){
        return getTypeSpecBuilder().build();
    }

    public void addMethod(MethodSpec method){
        this.typeSpecBuilder.addMethod(method);
    }

    public void addType(TypeSpec type) {
        this.typeSpecBuilder.addType(type);
    }
}