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
        if (data != null ||
                (setter !=null &&
                        (setter.value() == AccessLevel.PUBLIC ||
                                setter.value() == AccessLevel.PROTECTED ||
                                setter.value() == null))){
            return ElementFilter.fieldsIn(element.getEnclosedElements()).stream()
                    .filter(field-> !field.getModifiers().contains(Modifier.STATIC))
                    .filter(field -> {
                        Setter fieldSetter = field.getAnnotation(Setter.class);
                        return fieldSetter == null ||
                                (fieldSetter.value() == AccessLevel.PUBLIC ||
                                        fieldSetter.value() == AccessLevel.PROTECTED ||
                                        fieldSetter.value() == null);
                    })
                    .map(field -> parseFromElement(field))
                    .collect(Collectors.toList());
        }else {
            return ElementFilter.fieldsIn(element.getEnclosedElements()).stream()
                    .filter(field->{
                        Setter fieldSetter = field.getAnnotation(Setter.class);
                        return fieldSetter != null &&  (fieldSetter.value() == AccessLevel.PUBLIC ||
                                fieldSetter.value() == AccessLevel.PROTECTED ||
                                fieldSetter.value() == null);
                    })
                    .map(field ->  parseFromElement(field))
                    .collect(Collectors.toList());
        }

    }

    protected abstract SetterMeta metaFor(VariableElement element);
    private SetterMeta parseFromElement(VariableElement element){
        SetterMeta setterMeta = metaFor(element);

        setterMeta.setName(element.getSimpleName().toString());

        setterMeta.setType(TypeName.get(element.asType()));

        Description description = element.getAnnotation(Description.class);
        if (description != null){
            setterMeta.setDescription(description.value());
        }

       return setterMeta;

    }
}
