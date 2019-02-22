package com.geekhalo.ddd.lite.codegen.springdatarepository;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import lombok.Getter;

import javax.lang.model.element.VariableElement;

@Getter
public final class ParamElement{
    private final boolean field;
    private final TypeName typeName;
    private final String name;

    ParamElement(VariableElement element){
        this.typeName = TypeName.get(element.asType());
        this.name = element.getSimpleName().toString();
        this.field = true;
    }

    ParamElement(Class type, String name){
        this.typeName = ClassName.get(type);
        this.name = name;
        this.field = false;
    }
}