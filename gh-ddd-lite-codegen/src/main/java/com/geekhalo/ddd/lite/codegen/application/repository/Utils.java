package com.geekhalo.ddd.lite.codegen.application.repository;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.springframework.data.domain.Page;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

class Utils {

    public static TypeName getReturnTypeName(TypeMirror typeMirror, RepositoryBasedMethodMeta methodMeta){
        if (isOptional(typeMirror)){
            return ParameterizedTypeName.get(ClassName.get(Optional.class), ClassName.get(methodMeta.getModelVoType()));
        }
        if (isPage(typeMirror)){
            return ParameterizedTypeName.get(ClassName.get(Page.class), ClassName.get(methodMeta.getModelVoType()));
        }
        if (isList(typeMirror)){
            return ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(methodMeta.getModelVoType()));
        }
        if (isModel(typeMirror, methodMeta)){
            return TypeName.get(methodMeta.getModelVoType().asType());
        }
        return TypeName.get(typeMirror);
    }

    public static TypeName getReturnTypeName(TypeMirror typeMirror, RepositoryBasedMethodMeta methodMeta, TypeName typeName){
        if (isOptional(typeMirror)){
            return ParameterizedTypeName.get(ClassName.get(Optional.class), typeName);
        }
        if (isPage(typeMirror)){
            return ParameterizedTypeName.get(ClassName.get(Page.class), typeName);
        }
        if (isList(typeMirror)){
            return ParameterizedTypeName.get(ClassName.get(List.class), typeName);
        }
        if (isModel(typeMirror, methodMeta)){
            return typeName;
        }
        return null;
    }

    public static boolean isOptional(TypeMirror typeMirror){
        return typeMirror.toString().startsWith(Optional.class.getName());
    }

    public static boolean isPage(TypeMirror typeMirror){
        return typeMirror.toString().startsWith(Page.class.getName());
    }

    public static boolean isList(TypeMirror typeMirror){
        return typeMirror.toString().startsWith(List.class.getName());
    }

    public static boolean isModel(TypeMirror typeMirror, RepositoryBasedMethodMeta methodMeta){
        return typeMirror.equals(methodMeta.getModelType().asType());
    }

    public static String getConvertMethodName(TypeElement typeElement){
        return "convert" + typeElement.getSimpleName().toString();
    }

    public static String getConvertListMethodName(TypeElement typeElement){
        return "convert" + typeElement.getSimpleName().toString() + "List";
    }

    public static String getConvertPageMethodName(TypeElement typeElement){
        return "convvert" + typeElement.getSimpleName().toString() + "Page";
    }

}
