package com.geekhalo.ddd.lite.codegen.support.parser;

import com.geekhalo.ddd.lite.codegen.support.meta.ModelMethodMeta;

import javax.lang.model.element.TypeElement;
import java.util.List;

public interface ModelMethodMetaParser<M extends ModelMethodMeta> {

     List<M> parse(TypeElement typeElement);
}
