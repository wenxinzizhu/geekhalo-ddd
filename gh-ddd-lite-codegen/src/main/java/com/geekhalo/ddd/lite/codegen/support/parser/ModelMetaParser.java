package com.geekhalo.ddd.lite.codegen.support.parser;

import com.geekhalo.ddd.lite.codegen.support.meta.ModelMeta;
import com.geekhalo.ddd.lite.codegen.support.meta.ModelMethodMeta;

import javax.lang.model.element.TypeElement;

public abstract class ModelMetaParser<MethodMeta extends ModelMethodMeta,
        M extends ModelMeta<MethodMeta>,
        MP extends ModelMethodMetaParser<MethodMeta>> {

    protected abstract M modelFor(TypeElement typeElement);

    protected abstract MP[] parsers();

    public final M parse(TypeElement typeElement){
        M m = modelFor(typeElement);
        for (MP mp : parsers()){
            mp.parse(typeElement).forEach(methodMeta -> m.addMethodMeta(methodMeta));
        }
        return m;
    }
}
