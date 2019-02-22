package com.geekhalo.ddd.lite.codegen.support.meta;

import com.squareup.javapoet.TypeName;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter(AccessLevel.PUBLIC)
public abstract class ModelGetterMeta extends ModelMethodMeta {
    private TypeName type;

    public final TypeName type() {
        return this.type;
    }

    protected void merge(ModelGetterMeta methodMeta) {
        super.merge(methodMeta);
        if (methodMeta.ignore()){
            setIgnore(true);
        }
        if (StringUtils.isNotEmpty(methodMeta.description())){
            setDescription(methodMeta.description());
        }
    }
}
