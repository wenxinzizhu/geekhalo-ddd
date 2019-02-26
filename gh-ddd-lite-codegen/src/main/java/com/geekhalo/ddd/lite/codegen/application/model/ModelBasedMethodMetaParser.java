package com.geekhalo.ddd.lite.codegen.application.model;

import com.geekhalo.ddd.lite.codegen.TypeCollector;
import com.geekhalo.ddd.lite.codegen.application.GenApplicationIgnore;
import com.google.common.collect.Sets;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.Set;
import java.util.stream.Collectors;

import static com.geekhalo.ddd.lite.codegen.utils.MethodUtils.isSetter;
import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.getIdClassName;

public final class ModelBasedMethodMetaParser {


    public ModelBasedMethodMeta parse(TypeElement modelType, TypeCollector typeCollector) {
        String idClassNameStr = getIdClassName(modelType);
        ModelBasedMethodMeta metadata = new ModelBasedMethodMeta(ClassName.bestGuess(idClassNameStr), modelType);
        TypeElement elementToCollect = modelType;
        Set<String> methods = Sets.newHashSet();
        do {
            collect(elementToCollect, metadata, methods);
            TypeMirror typeMirror = elementToCollect.getSuperclass();
            String type = typeMirror.toString();
            elementToCollect = typeCollector.getByName(type);
        }while (elementToCollect != null);

        return metadata;
    }

    private void collect(TypeElement modelType, ModelBasedMethodMeta metadata, Set<String> methods) {
        Set<ExecutableElement> executableElements = ElementFilter.methodsIn(modelType.getEnclosedElements()).stream()
                .filter(executableElement -> !isSetter(executableElement))
                .filter(executableElement -> executableElement.getModifiers().contains(Modifier.PUBLIC))
                .filter(executableElement -> executableElement.getAnnotation(GenApplicationIgnore.class) == null)
                .collect(Collectors.toSet());

        for (ExecutableElement executableElement : executableElements){
            String key = getKeyFromElement(executableElement);
            if (!methods.contains(key)) {
                methods.add(key);
                if (isFactoryMethod(modelType, executableElement)) {
                    metadata.addCreateMethod(executableElement);
                } else if (isBizMethod(executableElement)) {
                    metadata.addUpdateMethod(executableElement);
                }
            }
        }
    }

    private String getKeyFromElement(ExecutableElement executableElement) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(executableElement.getSimpleName().toString());
        stringBuilder.append("#");
        for(VariableElement element : executableElement.getParameters()){
            stringBuilder.append(element.asType().toString()).append("-");
        }
        return stringBuilder.toString();
    }

    private boolean isBizMethod(ExecutableElement executableElement) {
        return "void".equals(executableElement.getReturnType().toString());
    }

    private boolean isFactoryMethod(Element element, ExecutableElement executableElement) {
        return executableElement.getReturnType() == element.asType() && executableElement.getModifiers().contains(Modifier.STATIC);
    }
}
