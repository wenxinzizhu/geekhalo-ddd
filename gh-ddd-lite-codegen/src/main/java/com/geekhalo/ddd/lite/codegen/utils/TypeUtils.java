package com.geekhalo.ddd.lite.codegen.utils;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.domain.support.jpa.IdentitiedJpaAggregate;
import com.geekhalo.ddd.lite.domain.support.jpa.IdentitiedJpaEntity;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaAggregate;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaEntity;
import com.squareup.javapoet.*;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.stream.Collectors;

public class TypeUtils {
    public static String getParentPacketName(String pkgName){
        return  pkgName.substring(0, pkgName.lastIndexOf('.'));
    }



    public static void bindDescription(Element element, MethodSpec.Builder methodBuilder) {
        Description description = element.getAnnotation(Description.class);
        if (description != null){
            methodBuilder.addAnnotation(AnnotationSpec.builder(Description.class)
                    .addMember("value", "\"" + description.value() + "\"")
                    .build());
        }
    }

    public static ParameterSpec createParameterSpecFromElement(VariableElement element){
        TypeName type = TypeName.get(element.asType());
        String name = element.getSimpleName().toString();
        ParameterSpec.Builder parameterBuilder = ParameterSpec.builder(type, name)
                .addModifiers(element.getModifiers());
        Description description = element.getAnnotation(Description.class);
        if (description != null){
            parameterBuilder.addAnnotation(AnnotationSpec.builder(Description.class)
                    .addMember("value", "\"" + description.value() + "\"")
                    .build());
        }
        return parameterBuilder.build();
    }

    public static ParameterSpec createIdParameter(ClassName idClassName){
        return ParameterSpec.builder(idClassName, "id")
                .addAnnotation(AnnotationSpec.builder(Description.class)
                        .addMember("value", "\"主键\"")
                        .build())
                .build();
    }

    public static String createParamListStr(ExecutableElement executableElement, String... beforeParam) {
        List<String> parm = executableElement.getParameters().stream()
                .map(varElement -> varElement.getSimpleName().toString())
                .collect(Collectors.toList());
        for (int i = beforeParam.length-1; i >= 0; i--) {
            parm.add(0, beforeParam[i]);
        }
        return parm.stream()
                .collect(Collectors.joining(", "));
    }

    public static String createParamVarStr(ExecutableElement executableElement){
        return executableElement.getParameters().stream()
                .map(varElement -> "{}")
                .collect(Collectors.joining(", "));
    }

    public static String getNameFromGetter(String s) {
        String r = null;
        if (s.startsWith("get")) {
            r = s.substring(3, s.length());
        } else if (s.startsWith("is")) {
            r = s.substring(2, s.length());
        } else {
            r = s;
        }
        return r.substring(0, 1).toLowerCase() + r.substring(1, r.length());
    }

    public static String getNameFromSetter(String s) {
        String r = null;
        if (s.startsWith("set")) {
            r = s.substring(3, s.length());
        } else {
            r = s;
        }
        return r.substring(0, 1).toLowerCase() + r.substring(1, r.length());
    }

    public static boolean isGetter(ExecutableElement element) {
        String s = element.getSimpleName().toString();
        TypeMirror typeMirror = element.getReturnType();
        boolean is = s.startsWith("is") && typeMirror.getKind() == TypeKind.BOOLEAN;
        boolean getter = s.startsWith("get") && typeMirror.getKind() != TypeKind.VOID && !element.getModifiers().contains(Modifier.STATIC);
        return is || getter;
    }


    public static String getFieldName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
    }

    public static String getIdClassName(TypeElement typeElement){
        TypeMirror typeMirror = typeElement.getSuperclass();
        if (typeMirror == null){
            return null;
        }

        String parentCls = typeMirror.toString();
        if (parentCls.startsWith(JpaAggregate.class.getName())){
            return "java.lang.Long";
        }
        if (parentCls.startsWith(JpaEntity.class.getName())){
            return "java.lang.Long";
        }
        if (parentCls.startsWith(IdentitiedJpaAggregate.class.getName())){
            return getIdClassFrom(parentCls);
        }
        if (parentCls.startsWith(IdentitiedJpaEntity.class.getName())){
            return getIdClassFrom(parentCls);
        }
        return null;
    }

    private static String getIdClassFrom(String parentCls) {
        return parentCls.substring(parentCls.indexOf("<") + 1, parentCls.indexOf(">"));
    }
}
