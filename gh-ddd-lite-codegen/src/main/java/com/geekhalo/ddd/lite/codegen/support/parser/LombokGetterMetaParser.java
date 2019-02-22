package com.geekhalo.ddd.lite.codegen.support.parser;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.support.meta.ModelGetterMeta;
import com.squareup.javapoet.TypeName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.List;
import java.util.stream.Collectors;

public abstract class LombokGetterMetaParser<GetterMeta extends ModelGetterMeta> implements ModelMethodMetaParser<GetterMeta> {
    @Override
    public final List<GetterMeta> parse(TypeElement element) {
        Data data = element.getAnnotation(Data.class);
        Getter getter = element.getAnnotation(Getter.class);
        if (data != null || (getter !=null && getter.value() == AccessLevel.PUBLIC)){
            return ElementFilter.fieldsIn(element.getEnclosedElements()).stream()
                    .filter(field-> !field.getModifiers().contains(Modifier.STATIC))
                    .filter(field -> {
                        Getter fieldGetter = field.getAnnotation(Getter.class);
                        return fieldGetter == null || fieldGetter.value() == AccessLevel.PUBLIC;
                    })
                    .map(field -> parseFromElement(field))
                    .collect(Collectors.toList());
        }else {
            return ElementFilter.fieldsIn(element.getEnclosedElements()).stream()
                    .filter(field->{
                        Getter fieldGetter = field.getAnnotation(Getter.class);
                        return fieldGetter != null && fieldGetter.value() == AccessLevel.PUBLIC;
                    })
                    .map(field ->  parseFromElement(field))
                    .collect(Collectors.toList());
        }

    }

    protected abstract GetterMeta metaFor(VariableElement element);
    private GetterMeta parseFromElement(VariableElement element){
        GetterMeta getterMeta = metaFor(element);

        getterMeta.setName(element.getSimpleName().toString());

        getterMeta.setType(TypeName.get(element.asType()));

        Description description = element.getAnnotation(Description.class);
        if (description != null){
            getterMeta.setDescription(description.value());
        }

       return getterMeta;

    }
}
