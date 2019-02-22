package com.geekhalo.ddd.lite.codegen.application;

import com.geekhalo.ddd.lite.codegen.support.TypeBuilderFactory;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public final class ApplicationBuilderFactory implements TypeBuilderFactory {
    private final String ifcName;

    public ApplicationBuilderFactory(String ifcName) {
        this.ifcName = ifcName;
    }

    @Override
    public TypeSpec.Builder create() {
        return TypeSpec.interfaceBuilder(ifcName)
                .addModifiers(Modifier.PUBLIC);
    }
}
