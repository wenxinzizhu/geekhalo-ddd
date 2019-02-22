package com.geekhalo.ddd.lite.codegen.updater.support.meta;

import com.geekhalo.ddd.lite.codegen.support.meta.ModelSetterMeta;
import lombok.AccessLevel;
import lombok.Setter;

@Setter(AccessLevel.PRIVATE)
public class UpdaterSetterMeta extends ModelSetterMeta {

    public void merge(UpdaterSetterMeta meta){
        super.merge(meta);
    }
}
