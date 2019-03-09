package com.geekhalo.ddd.lite.codegen.controller.request;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.google.common.collect.Lists;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.List;

public class RequestBodyInfoUtils {

    public static RequestBodyInfo parseAndCreateForUpdate(ExecutableElement executableElement, String pkgName, RequestBodyCreator creator) {
        List<? extends VariableElement> params = executableElement.getParameters();
        if (params.size() == 1){
            return null;
        }
        if (params.size() == 2 && notSimpleType(params.get(1))){
            return new SingleRequestBody(params.get(1));
        }

        String name = executableElement.getSimpleName().toString();
        String clsName = name.substring(0, 1).toUpperCase() + name.substring(1) + "Req";
        TypeSpec.Builder clsBuilder = TypeSpec.classBuilder(clsName);
        clsBuilder.addAnnotation(Data.class);
        clsBuilder.addAnnotation(ApiModel.class);
        List<String> paramsWithoutId = Lists.newArrayList();
        executableElement.getParameters().stream()
                .filter(element-> !"id".equalsIgnoreCase(element.getSimpleName().toString()))
                .forEach(element->{
                    Description description = element.getAnnotation(Description.class);
                    String descrStr = description != null ? description.value() : "";
                    clsBuilder.addField(FieldSpec.builder(ClassName.get(element.asType()), element.getSimpleName().toString(), Modifier.PRIVATE)
                            .addAnnotation(AnnotationSpec.builder(ApiModelProperty.class)
                                    .addMember("value", "\""+ descrStr + "\"")
                                    .build())
                            .build());
                    paramsWithoutId.add(element.getSimpleName().toString());
                });
        creator.apply(pkgName, clsName, clsBuilder);

        return new CreatedRequestBody(pkgName + "." + clsName, paramsWithoutId);
    }

    public static RequestBodyInfo parseAndCreateOfAllParams(ExecutableElement executableElement, String pkgName, RequestBodyCreator creator) {
        List<? extends VariableElement> params = executableElement.getParameters();
        if (params.size() == 0){
            return null;
        }
        if (params.size() == 1 && notSimpleType(params.get(0))){
            return new SingleRequestBody(params.get(0));
        }
        String name = executableElement.getSimpleName().toString();
        String clsName = name.substring(0, 1).toUpperCase() + name.substring(1) + "Req";
        TypeSpec.Builder clsBuilder = TypeSpec.classBuilder(clsName);
        clsBuilder.addAnnotation(Data.class);
        clsBuilder.addAnnotation(ApiModel.class);
        List<String> paramsWithoutId = Lists.newArrayList();
        executableElement.getParameters().stream()
                .forEach(element->{
                    Description description = element.getAnnotation(Description.class);
                    String descrStr = description != null ? description.value() : "";
                    clsBuilder.addField(FieldSpec.builder(ClassName.get(element.asType()), element.getSimpleName().toString(), Modifier.PRIVATE)
                            .addAnnotation(AnnotationSpec.builder(ApiModelProperty.class)
                                    .addMember("value", "\""+ descrStr + "\"")
                                    .build())
                            .build());
                    paramsWithoutId.add(((VariableElement) element).getSimpleName().toString());
                });
        creator.apply(pkgName, clsName, clsBuilder);
        return new CreatedRequestBody(pkgName + "." + clsName, paramsWithoutId);
    }


    public static RequestBodyInfo parseAndCreateForPage(ExecutableElement executableElement, String pkgName, RequestBodyCreator creator) {
        List<? extends VariableElement> params = executableElement.getParameters();
        if (params.size() == 0){
            return null;
        }
        if (params.size() == 1 && notSimpleType(params.get(0))){
            VariableElement variableElement = params.get(0);
            if ("org.springframework.data.domain.Pageable".equalsIgnoreCase(variableElement.asType().toString())){
                return new SingleRequestBody(ClassName.bestGuess("com.geekhalo.ddd.lite.spring.mvc.PageableImpl"), variableElement.getSimpleName().toString());
            }
            return new SingleRequestBody(params.get(0));
        }
        String name = executableElement.getSimpleName().toString();
        String clsName = name.substring(0, 1).toUpperCase() + name.substring(1) + "Req";
        TypeSpec.Builder clsBuilder = TypeSpec.classBuilder(clsName);
        clsBuilder.addAnnotation(Data.class);
        clsBuilder.addAnnotation(ApiModel.class);
        List<String> paramList = Lists.newArrayList();
        executableElement.getParameters().stream()
                .forEach(element->{
                    Description description = element.getAnnotation(Description.class);
                    String descrStr = description != null ? description.value() : "";
                    if (!"org.springframework.data.domain.Pageable".equalsIgnoreCase(element.asType().toString())) {
                        clsBuilder.addField(FieldSpec.builder(ClassName.get(element.asType()), element.getSimpleName().toString(), Modifier.PRIVATE)
                                .addAnnotation(AnnotationSpec.builder(ApiModelProperty.class)
                                        .addMember("value", "\"" + descrStr + "\"")
                                        .build())
                                .build());
                        paramList.add(((VariableElement) element).getSimpleName().toString());
                    }else {
                        clsBuilder.addField(FieldSpec.builder(ClassName.bestGuess("com.geekhalo.ddd.lite.spring.mvc.PageableImpl"), element.getSimpleName().toString(), Modifier.PRIVATE)
                                .addAnnotation(AnnotationSpec.builder(ApiModelProperty.class)
                                        .addMember("value", "\"" + descrStr + "\"")
                                        .build())
                                .build());
                        paramList.add(((VariableElement) element).getSimpleName().toString());
                    }
                });
        creator.apply(pkgName, clsName, clsBuilder);
        return new CreatedRequestBody(pkgName + "." + clsName, paramList);
    }


    private static boolean notSimpleType(VariableElement variableElement) {
        String name = variableElement.asType().toString();
        name = name.substring(name.lastIndexOf(".") + 1);
        return !("string".equalsIgnoreCase(name) ||
                "int".equalsIgnoreCase(name) ||
                "integer".equalsIgnoreCase(name) ||
                "long".equalsIgnoreCase(name) ||
                "date".equalsIgnoreCase(name)||
                "instant".equalsIgnoreCase(name));
    }


    public interface RequestBodyCreator{
        void apply(JavaSource javaSource);
        default void apply(String pkgName, String clsName, TypeSpec.Builder builder){
            apply(new JavaSource(pkgName, clsName, builder));
        }
    }
}
