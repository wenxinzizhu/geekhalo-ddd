package com.geekhalo.ddd.lite.codegen.creator.support.meta;

import com.geekhalo.ddd.lite.codegen.support.meta.ModelSetterMeta;
import lombok.AccessLevel;
import lombok.Setter;

@Setter(AccessLevel.PRIVATE)
public class CreatorSetterMeta extends ModelSetterMeta {

    public void merge(CreatorSetterMeta meta){
        super.merge(meta);
    }
}
