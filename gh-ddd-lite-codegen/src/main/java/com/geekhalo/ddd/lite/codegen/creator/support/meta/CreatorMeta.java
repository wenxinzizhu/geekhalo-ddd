package com.geekhalo.ddd.lite.codegen.creator.support.meta;

import com.geekhalo.ddd.lite.codegen.support.AccessLevel;
import com.geekhalo.ddd.lite.codegen.support.meta.ModelMeta;

import javax.lang.model.element.TypeElement;

public class CreatorMeta extends ModelMeta<CreatorSetterMeta> {
    public CreatorMeta(TypeElement typeElement) {
        super(typeElement);
    }

    @Override
    protected boolean accept(CreatorSetterMeta setterMeta) {
        return setterMeta.accessLevel() == AccessLevel.PUBLIC || setterMeta.accessLevel() == AccessLevel.PROTECTED;
    }

    @Override
    protected void merge(CreatorSetterMeta meta, CreatorSetterMeta n){
        meta.merge(n);
    }
}
