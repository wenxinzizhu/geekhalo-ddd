package com.geekhalo.ddd.lite.codegen.updater.support.meta;

import com.geekhalo.ddd.lite.codegen.support.AccessLevel;
import com.geekhalo.ddd.lite.codegen.support.meta.ModelMeta;

import javax.lang.model.element.TypeElement;

public class UpdaterMeta extends ModelMeta<UpdaterSetterMeta> {
    public UpdaterMeta(TypeElement typeElement) {
        super(typeElement);
    }

    @Override
    protected boolean accept(UpdaterSetterMeta updaterSetterMeta) {
        return updaterSetterMeta.accessLevel() == AccessLevel.PUBLIC;
    }

    @Override
    protected void merge(UpdaterSetterMeta meta, UpdaterSetterMeta n){
        meta.merge(n);
    }
}
