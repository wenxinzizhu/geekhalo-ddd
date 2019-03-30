package com.geekhalo.ddd.lite.codegen.application.repository;

import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.support.MethodWriter;
import com.squareup.javapoet.MethodSpec;
import lombok.Data;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import static com.geekhalo.ddd.lite.codegen.application.Utils.getApplicationMethodName;
import static com.geekhalo.ddd.lite.codegen.application.repository.Utils.getReturnTypeName;
import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.bindDescription;
import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.createParameterSpecFromElement;

@Data
public final class RepositoryBasedApplicationMethodWriter implements MethodWriter {
    private final RepositoryBasedMethodMeta methodMeta;

    @Override
    public void writeTo(JavaSource javaSource) {
        methodMeta.getQueryMethods()
                .forEach(executableElement -> javaSource.addMethod(createQueryMethod(executableElement)));
    }

    private MethodSpec createQueryMethod(ExecutableElement executableElement) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getApplicationMethodName(executableElement))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(getReturnTypeName(executableElement.getReturnType(), methodMeta));
        bindDescription(executableElement, methodBuilder);
        executableElement.getParameters().forEach(variableElement -> {
            methodBuilder.addParameter(createParameterSpecFromElement(variableElement));

        });
        return methodBuilder.build();
    }


}
