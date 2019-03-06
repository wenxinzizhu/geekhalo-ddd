package com.geekhalo.ddd.lite.codegen.support.parser;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.support.meta.ModelSetterMeta;
import com.squareup.javapoet.TypeName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.List;
import java.util.stream.Collectors;

public abstract class LombokSetterMetaParser<SetterMeta extends ModelSetterMeta> implements ModelMethodMetaParser<SetterMeta> {

    @Override
    public final List<SetterMeta> parse(TypeElement element) {
        Data data = element.getAnnotation(Data.class);
        Setter setter = element.getAnnotation(Setter.class);
        return ElementFilter.fieldsIn(element.getEnclosedElements()).stream()
                .filter(field-> !field.getModifiers().contains(Modifier.STATIC))
                .map(field -> {
                    Setter fieldSetter = field.getAnnotation(Setter.class);
                    if (fieldSetter != null && fieldSetter.value() != null){
                        return parseFromElement(field, fieldSetter.value());
                    }
                    if (setter != null && setter.value() != null){
                        return parseFromElement(field, setter.value());
                    }
                    if (data != null){
                        return parseFromElement(field, AccessLevel.PUBLIC);
                    }
                    return null;
                })
                .filter(e -> e != null)
                .collect(Collectors.toList());

    }

    protected abstract SetterMeta metaFor(VariableElement element);
    private SetterMeta parseFromElement(VariableElement element, AccessLevel accessLevel){
        SetterMeta setterMeta = metaFor(element);

        setterMeta.setName(element.getSimpleName().toString());

        setterMeta.setType(TypeName.get(element.asType()));

        setterMeta.setAccessLevel(com.geekhalo.ddd.lite.codegen.support.AccessLevel.getFromAccessLecel(accessLevel));

        Description description = element.getAnnotation(Description.class);
        if (description != null){
            setterMeta.setDescription(description.value());
        }

       return setterMeta;

    }
}
