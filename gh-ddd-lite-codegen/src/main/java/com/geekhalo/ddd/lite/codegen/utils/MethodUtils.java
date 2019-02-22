package com.geekhalo.ddd.lite.codegen.utils;

import com.geekhalo.ddd.lite.codegen.CreateMethod;
import com.geekhalo.ddd.lite.codegen.QueryMethod;
import com.geekhalo.ddd.lite.codegen.UpdateMethod;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.stream.Collectors;

public class MethodUtils {
    public static boolean isCreateMethod(ExecutableElement method){
        if (hasAnnotation(method, CreateMethod.class)){
            return true;
        }
        String methodName = method.getSimpleName().toString();
        return methodName.startsWith("create");
    }



    public static boolean isUpdateMethod(ExecutableElement method){
        if (hasAnnotation(method, UpdateMethod.class)){
            return true;
        }
        return "void".equalsIgnoreCase(method.getReturnType().toString());
    }

    public static boolean isQueryMethod(ExecutableElement method){
        if (hasAnnotation(method, QueryMethod.class)){
            return true;
        }
        return isGetByIdMethod(method) || isListMethod(method) || isPageMethod(method);
    }


    public static boolean isGetByIdMethod(ExecutableElement executableElement) {
        return executableElement.getReturnType().toString().startsWith("java.util.Optional")
                && executableElement.getParameters().size() == 1
                && "getById".equalsIgnoreCase(executableElement.getSimpleName().toString());
    }

    public static boolean isListMethod(ExecutableElement executableElement) {
        String returnType = executableElement.getReturnType().toString();
        return returnType.startsWith("java.util.List");
    }

    public static boolean isPageMethod(ExecutableElement executableElement) {
        String returnType = executableElement.getReturnType().toString();
        return returnType.startsWith("org.springframework.data.domain.Page");
    }


    private static boolean hasAnnotation(ExecutableElement method, Class annotaion) {
        return method.getAnnotation(annotaion) != null;
    }

    public static boolean isSetter(ExecutableElement element){
        String name = element.getSimpleName().toString();
        List<? extends VariableElement> parameters = element.getParameters();
        return name.startsWith("set") && parameters.size() == 1;
    }

    public static String createParamListStr(ExecutableElement executableElement, String... beforeParam) {
        List<String> parm = executableElement.getParameters().stream()
                .map(varElement -> varElement.getSimpleName().toString())
                .collect(Collectors.toList());
        return createParamListStr(parm, beforeParam);
    }


    public static String createParamListStr(List<String> params, String... beforeParam) {
        List<String> parm = Lists.newArrayList(params);
        for (int i = beforeParam.length-1; i >= 0; i--) {
            parm.add(0, beforeParam[i]);
        }
        return parm.stream()
                .collect(Collectors.joining(", "));
    }

    public static String getMethodKey(ExecutableElement method){
        StringBuilder stringBuilder = new StringBuilder(method.getSimpleName().toString());
        if (!CollectionUtils.isEmpty(method.getParameters())){
            stringBuilder.append(method.getParameters().stream()
                    .map(element->((VariableElement) element).asType().toString())
                    .collect(Collectors.joining("-")));
        }
        return stringBuilder.toString();

    }
}
