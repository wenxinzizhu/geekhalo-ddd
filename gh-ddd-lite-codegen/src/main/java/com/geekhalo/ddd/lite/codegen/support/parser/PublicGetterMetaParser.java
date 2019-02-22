package com.geekhalo.ddd.lite.codegen.support.parser;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.support.meta.ModelGetterMeta;
import com.geekhalo.ddd.lite.codegen.dto.GenDtoIgnore;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.List;
import java.util.stream.Collectors;

import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.getNameFromGetter;
import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.isGetter;

public abstract class PublicGetterMetaParser<GetterMeta extends ModelGetterMeta> implements ModelMethodMetaParser<GetterMeta>{

    @Override
    public final List<GetterMeta> parse(TypeElement typeElement) {
        return ElementFilter.methodsIn(typeElement.getEnclosedElements()).stream()
                .filter(field-> !field.getModifiers().contains(Modifier.STATIC))
                .filter(method->{
                    boolean isPublic = method.getModifiers().contains(Modifier.PUBLIC);
                    return isPublic && isGetter(method);
                })
                .map(method-> parseFromElement(method))
                .collect(Collectors.toList());
    }

    protected abstract GetterMeta metaFor(ExecutableElement executableElement);

    private GetterMeta parseFromElement(ExecutableElement executableElement){
        GetterMeta getterMeta = metaFor(executableElement);

        String name = getNameFromGetter(executableElement.getSimpleName().toString());
        getterMeta.setName(name);
        getterMeta.setType(TypeName.get(executableElement.getReturnType()));

        GenDtoIgnore voIgnore = executableElement.getAnnotation(GenDtoIgnore.class);
        if (voIgnore != null){
            getterMeta.setIgnore(true);
        }

        Description description = executableElement.getAnnotation(Description.class);
        if (description != null){
            getterMeta.setDescription(description.value());
        }
        return getterMeta;
    }
}
