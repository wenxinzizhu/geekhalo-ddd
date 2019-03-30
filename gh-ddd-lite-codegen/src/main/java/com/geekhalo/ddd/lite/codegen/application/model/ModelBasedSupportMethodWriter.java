package com.geekhalo.ddd.lite.codegen.application.model;

import com.geekhalo.ddd.lite.codegen.ContainerManaged;
import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.support.MethodWriter;
import com.geekhalo.ddd.lite.codegen.utils.TypeUtils;
import com.google.common.collect.Lists;
import com.squareup.javapoet.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.geekhalo.ddd.lite.codegen.application.Utils.createApplicationParameter;
import static com.geekhalo.ddd.lite.codegen.application.Utils.getApplicationMethodName;
import static com.geekhalo.ddd.lite.codegen.application.Utils.getRepositoryGetterName;
import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.*;

public final class ModelBasedSupportMethodWriter implements MethodWriter {
    private final ModelBasedMethodMeta methodMeta;

    public ModelBasedSupportMethodWriter(ModelBasedMethodMeta methodMeta) {
        this.methodMeta = methodMeta;
    }

    @Override
    public void writeTo(JavaSource javaSource) {
        this.methodMeta.getCreateMethods()
                .forEach(executableElement-> writeCreateImpl(executableElement, javaSource));

        this.methodMeta.getUpdateMethods()
                .forEach(executableElement -> writeUpdateImpl(executableElement, javaSource));
    }

    private void writeCreateImpl(ExecutableElement executableElement, JavaSource javaSource){
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getApplicationMethodName(executableElement))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Transactional.class)
                .returns(this.methodMeta.getIdClassName());
        bindDescription(executableElement, methodBuilder);

        executableElement.getParameters().stream()
                .map(variableElement-> createApplicationParameter(variableElement))
                .filter(parameterSpec -> parameterSpec != null)
                .forEach(parameterSpec ->
                    methodBuilder.addParameter(parameterSpec)
                );

        String repositoryGetterName = getRepositoryGetterName(this.methodMeta.getModelType());

        List<String> params = executableElement.getParameters().stream()
                .map(param-> getCallParam(javaSource, param))
                .collect(Collectors.toList());
        String paramStr = params.stream().collect(Collectors.joining(","));

        CodeBlock.Builder codeBuilder = CodeBlock.builder()
                .add("\t$T result = creatorFor(this.$L())\n" +
                                "            .publishBy(getDomainEventBus())\n" +
                                "            .instance(() -> $T.$L($L))" +
                                "            .call(); \n",
                        TypeName.get(methodMeta.getModelType().asType()),
                        repositoryGetterName,
                        TypeName.get(methodMeta.getModelType().asType()),
                        executableElement.getSimpleName().toString(),
                        paramStr
                );
        methodBuilder.addCode(codeBuilder.build());


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("logger().info(\"success to create {}");
        if (CollectionUtils.isNotEmpty(params)){
            stringBuilder.append(" using parm ")
                    .append(createParamVarStr(params));
        }
        stringBuilder.append("\",");
        stringBuilder.append("result.getId()");
        if (CollectionUtils.isNotEmpty(params)){
            stringBuilder.append(",")
                    .append(createParamListStr(params));
        }
        stringBuilder.append(")");
        methodBuilder.addStatement(stringBuilder.toString());


        methodBuilder.addStatement("return result.getId()");

        javaSource.addMethod(methodBuilder.build());
    }

    private String getCallParam(JavaSource javaSource, VariableElement param) {
        ContainerManaged containerManaged = param.getAnnotation(ContainerManaged.class);
        if (containerManaged != null){
            String type = TypeUtils.getClassNameFromFullClassName(param.asType().toString());
            String field = TypeUtils.getFieldNameFromType(type);
            String getter = "get" + type;
            if (!javaSource.hasField(field)){
                FieldSpec fieldSpec = FieldSpec.builder(ClassName.get(param.asType()), field)
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(Autowired.class)
                        .build();
                javaSource.addField(fieldSpec);
                MethodSpec getterSpec = MethodSpec.methodBuilder(getter)
                        .addModifiers(Modifier.PROTECTED)
                        .returns(ClassName.get(param.asType()))
                        .addStatement("return this.$L", field)
                        .build();
                javaSource.addMethod(getterSpec);
            }
            return getter + "()";
        }else {
            return  param.getSimpleName().toString();
        }
    }

    private void writeUpdateImpl(ExecutableElement executableElement, JavaSource javaSource) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getApplicationMethodName(executableElement))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Transactional.class)
                .returns(TypeName.VOID)
                .addParameter(createIdParameter(this.methodMeta.getIdClassName()));

        bindDescription(executableElement, methodBuilder);

        executableElement.getParameters().stream()
                .map(variableElement-> createApplicationParameter(variableElement))
                .filter(parameterSpec -> parameterSpec != null)
                .forEach(parameterSpec ->
                    methodBuilder.addParameter(parameterSpec)
                );

        List<String> params = executableElement.getParameters().stream()
                .map(param-> getCallParam(javaSource, param))
                .collect(Collectors.toList());
        String paramStr = params.stream().collect(Collectors.joining(","));

        String repositoryGetterName = getRepositoryGetterName(this.methodMeta.getModelType());
        CodeBlock.Builder codeBuilder = CodeBlock.builder()
                .add("\t$T result = updaterFor(this.$L())\n" +
                                "            .publishBy(getDomainEventBus())\n" +
                                "            .id(id)\n" +
                                "            .update(agg -> agg.$L($L))\n" +
                                "            .call(); \n",
                        TypeName.get(methodMeta.getModelType().asType()),
                        repositoryGetterName,
                        executableElement.getSimpleName().toString(),
                        paramStr
                );

        methodBuilder.addCode(codeBuilder.build());


        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("logger().info(\"success to " + executableElement.getSimpleName().toString() +" for {}");
        if (params.size() > 0) {
            stringBuilder.append(" using param ")
                    .append(createParamVarStr(params));
        }
        stringBuilder.append("\",");
        stringBuilder.append("id");
        if (params.size() > 0){
            stringBuilder.append(",")
                    .append(createParamListStr(params));
        }
        stringBuilder.append(")");
        methodBuilder.addStatement(stringBuilder.toString());

        javaSource.addMethod(methodBuilder.build());
    }

}
