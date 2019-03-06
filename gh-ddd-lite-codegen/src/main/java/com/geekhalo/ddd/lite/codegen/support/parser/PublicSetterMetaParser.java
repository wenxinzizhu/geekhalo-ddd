package com.geekhalo.ddd.lite.codegen.support.parser;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.dto.GenDtoIgnore;
import com.geekhalo.ddd.lite.codegen.support.AccessLevel;
import com.geekhalo.ddd.lite.codegen.support.meta.ModelSetterMeta;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.geekhalo.ddd.lite.codegen.utils.MethodUtils.isSetter;
import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.getNameFromSetter;

public abstract class PublicSetterMetaParser<SetterMeta extends ModelSetterMeta> implements ModelMethodMetaParser<SetterMeta>{
    @Override
    public final List<SetterMeta> parse(TypeElement typeElement) {
        return ElementFilter.methodsIn(typeElement.getEnclosedElements()).stream()
                .filter(field-> !field.getModifiers().contains(Modifier.STATIC))
                .filter(method -> isSetter(method))
                .map(method-> parseFromElement(method, method.getModifiers()))
                .collect(Collectors.toList());
    }

    protected abstract SetterMeta metaFor(ExecutableElement executableElement);

    private SetterMeta parseFromElement(ExecutableElement executableElement, Set<Modifier> modifiers){
        SetterMeta setterMeta = metaFor(executableElement);

        String name = getNameFromSetter(executableElement.getSimpleName().toString());
        setterMeta.setName(name);
        setterMeta.setType(TypeName.get(executableElement.getParameters().get(0).asType()));
        setterMeta.setAccessLevel(AccessLevel.getFromModifers(modifiers));

        GenDtoIgnore voIgnore = executableElement.getAnnotation(GenDtoIgnore.class);
        if (voIgnore != null){
            setterMeta.setIgnore(true);
        }

        Description description = executableElement.getAnnotation(Description.class);
        if (description != null){
            setterMeta.setDescription(description.value());
        }
        return setterMeta;
    }
}
