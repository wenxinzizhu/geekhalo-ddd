package com.geekhalo.ddd.lite.codegen.controller.parser;

import com.geekhalo.ddd.lite.codegen.controller.GenControllerMethodMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import static com.geekhalo.ddd.lite.codegen.utils.MethodUtils.*;

public final class GenControllerMethodMetaParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenControllerMethodMetaParser.class);

    public GenControllerMethodMeta parse(TypeElement application){
        GenControllerMethodMeta methodMeta = new GenControllerMethodMeta();
        ElementFilter.methodsIn(application.getEnclosedElements()).forEach(executableElement -> {
            if (isCreateMethod(executableElement)){
                methodMeta.addCreateMethod(executableElement);
            }else if (isUpdateMethod(executableElement)){
                methodMeta.addUpdateMethod(executableElement);
            }else if (isQueryMethod(executableElement)){
                methodMeta.addQueryMethod(executableElement);
            }
            LOGGER.warn("failed to check method type of {}", executableElement.getSimpleName());
        });

        return methodMeta;
    }

}
