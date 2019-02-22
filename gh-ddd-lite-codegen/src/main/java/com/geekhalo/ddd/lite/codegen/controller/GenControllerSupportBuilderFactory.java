package com.geekhalo.ddd.lite.codegen.controller;

import com.geekhalo.ddd.lite.codegen.support.TypeBuilderFactory;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public final class GenControllerSupportBuilderFactory implements TypeBuilderFactory {
    private final TypeElement applicationType;
    private final String supportName;
    private final String parentName;

    public GenControllerSupportBuilderFactory(TypeElement applicationType,
                                              String supportName,
                                              String parentName) {
        this.applicationType = applicationType;
        this.supportName = supportName;
        this.parentName = parentName;
    }

    @Override
    public TypeSpec.Builder create() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(supportName)
                            .addModifiers(Modifier.ABSTRACT);
        if (StringUtils.isNotEmpty(parentName)){
            builder.superclass(ClassName.bestGuess(this.parentName));
        }

        builder.addField(FieldSpec.builder(TypeName.get(this.applicationType.asType()), "application")
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(Autowired.class)
                .build());
        builder.addMethod(MethodSpec.methodBuilder("getApplication")
                .addModifiers(Modifier.PROTECTED)
                .returns(TypeName.get(this.applicationType.asType()))
                .addStatement("return this.application")
                .build());
        return builder;
    }
}
