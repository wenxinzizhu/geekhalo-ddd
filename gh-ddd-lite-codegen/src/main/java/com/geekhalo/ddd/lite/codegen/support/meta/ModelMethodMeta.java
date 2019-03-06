package com.geekhalo.ddd.lite.codegen.support.meta;

import lombok.AccessLevel;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter(AccessLevel.PUBLIC)
public abstract class ModelMethodMeta{
    private boolean ignore;
    private String name;
    private String description;
    private com.geekhalo.ddd.lite.codegen.support.AccessLevel accessLevel;

    public final com.geekhalo.ddd.lite.codegen.support.AccessLevel accessLevel(){
        return this.accessLevel;
    }

    public final boolean ignore() {
        return this.ignore;
    }

    public final String name() {
        return this.name;
    }

    public final String description() {
        return this.description == null ? "" : this.description;
    }

    protected void merge(ModelMethodMeta methodMeta) {
        if (methodMeta.ignore()){
            setIgnore(true);
        }
        if (StringUtils.isNotEmpty(methodMeta.description())){
            setDescription(methodMeta.description());
        }
        if (this.accessLevel == null){
            setAccessLevel(methodMeta.accessLevel());
        }
    }
}
