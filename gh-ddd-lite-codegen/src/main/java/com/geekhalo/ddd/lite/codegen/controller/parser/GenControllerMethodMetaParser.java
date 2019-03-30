package com.geekhalo.ddd.lite.codegen.controller.parser;

import com.geekhalo.ddd.lite.codegen.TypeCollector;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerIgnore;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerMethodMeta;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.List;

import static com.geekhalo.ddd.lite.codegen.utils.MethodUtils.*;

public final class GenControllerMethodMetaParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenControllerMethodMetaParser.class);
    private final TypeCollector typeCollector;

    public GenControllerMethodMetaParser(TypeCollector typeCollector) {
        this.typeCollector = typeCollector;
    }

    public GenControllerMethodMeta parse(TypeElement application){
        GenControllerMethodMeta methodMeta = new GenControllerMethodMeta();
        parseFromType(methodMeta, application);
        return methodMeta;
    }

    private void parseFromType(GenControllerMethodMeta methodMeta, TypeElement typeElement){
        if (typeElement == null){
            return;
        }
        ElementFilter.methodsIn(typeElement.getEnclosedElements())
                .stream()
                .filter(element -> element.getModifiers().contains(Modifier.PUBLIC))
                .filter(element -> element.getAnnotation(GenControllerIgnore.class) == null)
                .forEach(executableElement -> {
            if (isCreateMethod(executableElement)){
                methodMeta.addCreateMethod(executableElement);
            }else if (isUpdateMethod(executableElement)){
                methodMeta.addUpdateMethod(executableElement);
            }else if (isQueryMethod(executableElement)){
                methodMeta.addQueryMethod(executableElement);
            }else {
                LOGGER.warn("failed to check method type of {}", executableElement.getSimpleName());
            }
        });
        TypeMirror typeMirror = typeElement.getSuperclass();
        if (typeMirror != null){
            TypeElement ty = this.typeCollector.getByName(typeMirror.toString());
            parseFromType(methodMeta, ty);
        }
        List<? extends TypeMirror> typeMirrors = typeElement.getInterfaces();
        if (CollectionUtils.isNotEmpty(typeMirrors)){
            for (TypeMirror typeMirror1 : typeMirrors){
                if (typeMirror1 != null){
                    TypeElement ty = this.typeCollector.getByName(typeMirror1.toString());
                    parseFromType(methodMeta, ty);
                }
            }
        }

    }

}
