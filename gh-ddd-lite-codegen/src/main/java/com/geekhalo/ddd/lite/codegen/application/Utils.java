package com.geekhalo.ddd.lite.codegen.application;

import com.geekhalo.ddd.lite.codegen.ContainerManaged;
import com.geekhalo.ddd.lite.codegen.utils.TypeUtils;
import com.squareup.javapoet.ParameterSpec;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Optional;

public class Utils {
    public static String getRepositoryGetterName(TypeElement element){
        return "get" + element.getSimpleName().toString() + "Repository";
    }

    public static String getRepositoryFieldName(TypeElement element){
        String typeName = element.getSimpleName().toString();
        return typeName.substring(0,1).toLowerCase() + typeName.substring(1) + "Repository";
    }

    public static String getApplicationMethodName(ExecutableElement element){

        return Optional.ofNullable(element.getAnnotation(GenApplicationMethod.class))
                .map(GenApplicationMethod::methodName)
                .filter(StringUtils::isNotEmpty)
                .orElseGet(()-> element.getSimpleName().toString());
    }

    public static boolean isContainerManaged(VariableElement element){
        return element.getAnnotation(ContainerManaged.class) != null;
    }

    public static ParameterSpec createApplicationParameter(VariableElement element){
        if (isContainerManaged(element)){
            return null;
        }
        return TypeUtils.createParameterSpecFromElement(element);
    }
}
