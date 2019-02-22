package com.geekhalo.ddd.lite.codegen.springdatarepository;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Value;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.List;

@Value
public class GenRepositoryMeta {
    private final boolean useQueryDsl;
    private final String pkgName;
    private final String clsName;
    private final TypeElement aggType;
    private final List<IndexMeta> indices;

    @Getter
    public static final class IndexMeta{
        private final boolean unique;
        private final List<ParamElement> params = Lists.newArrayList();

        public IndexMeta(boolean unique) {
            this.unique = unique;
        }

        public void addParam(VariableElement filed){
            this.params.add(new ParamElement(filed));
        }
    }
}
