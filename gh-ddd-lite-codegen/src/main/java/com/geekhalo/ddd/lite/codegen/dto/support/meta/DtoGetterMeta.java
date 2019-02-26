package com.geekhalo.ddd.lite.codegen.dto.support.meta;

import com.geekhalo.ddd.lite.codegen.dto.GenDtoPropertyConvert;
import com.geekhalo.ddd.lite.codegen.support.meta.ModelGetterMeta;
import lombok.AccessLevel;
import lombok.Setter;

@Setter(AccessLevel.PUBLIC)
public class DtoGetterMeta extends ModelGetterMeta {
    private GenDtoPropertyConvert voPropertyConvert;

    public void merge(DtoGetterMeta methodMeta) {
        super.merge(methodMeta);
        if (methodMeta.voPropertyConvert() != null){
            setVoPropertyConvert(methodMeta.voPropertyConvert());
        }
    }
    public GenDtoPropertyConvert voPropertyConvert() {
        return this.voPropertyConvert;
    }
}
