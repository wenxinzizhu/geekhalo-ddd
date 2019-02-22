package com.geekhalo.ddd.lite.codegen.utils;

import com.geekhalo.ddd.lite.codegen.Description;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
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

    public static ParameterSpec createIdParameter(){
        return ParameterSpec.builder(TypeName.LONG.box(), "id")
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
}
