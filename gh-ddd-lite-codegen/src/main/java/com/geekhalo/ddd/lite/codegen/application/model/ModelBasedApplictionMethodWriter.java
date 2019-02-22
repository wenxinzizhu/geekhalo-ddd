package com.geekhalo.ddd.lite.codegen.application.model;

import com.geekhalo.ddd.lite.codegen.support.MethodWriter;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.*;

public final class ModelBasedApplictionMethodWriter implements MethodWriter {
    private final ModelBasedMethodMeta methodMeta;

    public ModelBasedApplictionMethodWriter(ModelBasedMethodMeta methodMeta) {
        this.methodMeta = methodMeta;
    }

    @Override
    public void writeTo(TypeSpec.Builder builder) {
        this.methodMeta.getCreateMethods().forEach(executableElement -> builder.addMethod(createCreateMethod(executableElement)));

        this.methodMeta.getUpdateMethods().forEach(executableElement -> builder.addMethod(createUpdateMethod(executableElement)));
    }

    private MethodSpec createCreateMethod(ExecutableElement executableElement){
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypeName.get(methodMeta.getModelType().asType()));
        bindDescription(executableElement, methodBuilder);
        executableElement.getParameters().forEach(variableElement -> {
            methodBuilder.addParameter(createParameterSpecFromElement(variableElement));

        });
        return methodBuilder.build();
    }

    private MethodSpec createUpdateMethod(ExecutableElement executableElement){
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .addParameter(createIdParameter());
        bindDescription(executableElement, methodBuilder);
        executableElement.getParameters().forEach(variableElement -> {
            methodBuilder.addParameter(createParameterSpecFromElement(variableElement));

        });
        return methodBuilder.build();
    }
}
