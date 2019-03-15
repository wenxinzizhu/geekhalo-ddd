package com.geekhalo.ddd.lite.codegen.controller.request;

import com.geekhalo.ddd.lite.codegen.Description;
import com.google.common.collect.Lists;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.List;

public class CreatedRequestBody implements RequestBodyInfo {
    private final String parameterName = "body";
    private final String pkg;
    private final String outClsName;
    private final String reqClsName;
    private final List<String> callParams;
    private final TypeSpec.Builder subTypeBuilder;

    CreatedRequestBody(String pkg,
                       String outClsName,
                       String reqClsName) {
        this.pkg = pkg;
        this.outClsName = outClsName;
        this.reqClsName = reqClsName;

        this.callParams = Lists.newArrayList();
        this.subTypeBuilder = newSubTypeBuilder();
    }

    private TypeSpec.Builder newSubTypeBuilder() {
        TypeSpec.Builder clsBuilder = TypeSpec.classBuilder(reqClsName);
        clsBuilder.addModifiers(Modifier.STATIC, Modifier.PUBLIC);
        clsBuilder.addAnnotation(Data.class);
        clsBuilder.addAnnotation(ApiModel.class);
        return clsBuilder;
    }

    @Override
    public String getParameterName() {
        return parameterName;
    }

    @Override
    public TypeName getParameterType() {
        return ClassName.get(pkg, this.outClsName, this.reqClsName);
    }

    @Override
    public List<String> getCallParams() {
        return callParams;
    }

    @Override
    public List<TypeSpec.Builder> getBodyType() {
        return Arrays.asList(subTypeBuilder);
    }


    public void addParameters(List<? extends VariableElement> parameters) {
        for (VariableElement param : parameters){
            Description description = param.getAnnotation(Description.class);
            String descrStr = description != null ? description.value() : "";
            addParameter(TypeName.get(param.asType()), param.getSimpleName().toString(), descrStr);
        }
    }

    public void addParameter(TypeName typeName, String parameterName, String descStr){
        TypeName typeNameToUse = convertType(typeName);
        subTypeBuilder.addField(FieldSpec.builder(typeNameToUse, parameterName, Modifier.PRIVATE)
                .addAnnotation(AnnotationSpec.builder(ApiModelProperty.class)
                        .addMember("value", "\""+ descStr + "\"")
                        .build())
                .build());
        callParams.add(this.parameterName +
                ".get" + parameterName.substring(0, 1).toUpperCase() + parameterName.substring(1, parameterName.length()) + "()") ;
    }

    private TypeName convertType(TypeName typeName) {
        String typeCls = typeName.toString();
        if (typeCls.startsWith("org.springframework.data.domain.Pageable")){
            return ClassName.bestGuess("com.geekhalo.ddd.lite.spring.mvc.PageableImpl");
        }
        return typeName;
    }
}