package com.geekhalo.ddd.lite.codegen.support.meta;

import com.squareup.javapoet.TypeName;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter(AccessLevel.PUBLIC)
public abstract class ModelSetterMeta extends ModelMethodMeta {
    private TypeName type;
    public final TypeName type() {
        return this.type;
    }

    protected void merge(ModelSetterMeta n) {
        super.merge(n);
        if (n.ignore()){
            setIgnore(true);
        }
        if (StringUtils.isNotEmpty(n.description())){
            setDescription(n.description());
        }
    }
}
