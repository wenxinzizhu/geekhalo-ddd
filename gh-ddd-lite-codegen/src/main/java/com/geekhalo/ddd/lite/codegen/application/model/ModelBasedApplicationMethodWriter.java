package com.geekhalo.ddd.lite.codegen.application.model;

import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.support.MethodWriter;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import java.lang.reflect.ParameterizedType;

import static com.geekhalo.ddd.lite.codegen.application.Utils.createApplicationParameter;
import static com.geekhalo.ddd.lite.codegen.application.Utils.getApplicationMethodName;
import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.*;

public final class ModelBasedApplicationMethodWriter implements MethodWriter {
    private final ModelBasedMethodMeta methodMeta;

    public ModelBasedApplicationMethodWriter(ModelBasedMethodMeta methodMeta) {
        this.methodMeta = methodMeta;
    }

    @Override
    public void writeTo(JavaSource javaSource) {
        this.methodMeta.getCreateMethods().forEach(executableElement -> javaSource.addMethod(createCreateMethod(executableElement)));

        this.methodMeta.getUpdateMethods().forEach(executableElement -> javaSource.addMethod(createUpdateMethod(executableElement)));
    }

    private MethodSpec createCreateMethod(ExecutableElement executableElement){
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getApplicationMethodName(executableElement))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(this.methodMeta.getIdClassName());
        bindDescription(executableElement, methodBuilder);
        executableElement.getParameters().stream()
                .map(variableElement-> createApplicationParameter(variableElement))
                .filter(parameterSpec -> parameterSpec != null)
                .forEach(parameterSpec -> {
                    methodBuilder.addParameter(parameterSpec);

                });
        return methodBuilder.build();
    }

    private MethodSpec createUpdateMethod(ExecutableElement executableElement){
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getApplicationMethodName(executableElement))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .addParameter(createIdParameter(this.methodMeta.getIdClassName()));
        bindDescription(executableElement, methodBuilder);
        executableElement.getParameters().stream()
                .map(variableElement-> createApplicationParameter(variableElement))
                .filter(parameterSpec -> parameterSpec != null)
                .forEach(parameterSpec -> {
                    methodBuilder.addParameter(parameterSpec);

                });
        return methodBuilder.build();
    }
}
