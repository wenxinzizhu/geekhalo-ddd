package com.geekhalo.ddd.lite.codegen.application;

import javax.lang.model.element.TypeElement;

public class Utils {
    public static String getRepositoryGetterName(TypeElement element){
        return "get" + element.getSimpleName().toString() + "Repository";
    }

    public static String getRepositoryFieldName(TypeElement element){
        String typeName = element.getSimpleName().toString();
        return typeName.substring(0,1).toLowerCase() + typeName.substring(1) + "Repository";
    }
}
