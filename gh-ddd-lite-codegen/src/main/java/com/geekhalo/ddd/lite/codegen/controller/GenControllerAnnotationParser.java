package com.geekhalo.ddd.lite.codegen.controller;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.TypeElement;

@Value
public final class GenControllerAnnotationParser {
    private TypeElement typeElement;
    private GenController annotation;

    public String getControllerName(){
        return annotation.value();
    }

    public String getParentName(){
        return annotation.parentClass();
    }

    public boolean isWrapper(){
        return StringUtils.isNotEmpty(getWrapperCls());
    }

    public String getWrapperCls(){
        return annotation.wrapperCls();
    }

    public String getPkgName() {
        String controllerName = getControllerName();
        return controllerName.substring(0, controllerName.lastIndexOf("."));
    }
    public String getSimpleEndpointName(){
        String endpointName = getControllerName();
        return endpointName.substring(endpointName.lastIndexOf(".") + 1);
    }
}
