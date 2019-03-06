package com.geekhalo.ddd.lite.codegen.dto.support.meta;

import com.geekhalo.ddd.lite.codegen.support.AccessLevel;
import com.geekhalo.ddd.lite.codegen.support.meta.ModelMeta;

import javax.lang.model.element.TypeElement;

public class DtoMeta extends ModelMeta<DtoGetterMeta> {
    public DtoMeta(TypeElement type) {
        super(type);
    }

    @Override
    protected boolean accept(DtoGetterMeta meta) {
        return meta.accessLevel() == AccessLevel.PUBLIC;
    }

    @Override
    protected void merge(DtoGetterMeta meta, DtoGetterMeta n){
        meta.merge(n);
    }
}
