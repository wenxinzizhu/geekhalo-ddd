package com.geekhalo.ddd.lite.codegen.dto.support.parser;

import com.geekhalo.ddd.lite.codegen.dto.support.meta.DtoGetterMeta;
import com.geekhalo.ddd.lite.codegen.support.parser.LombokGetterMetaParser;

import javax.lang.model.element.VariableElement;

final class LombokDtoGetterMetaParser
        extends LombokGetterMetaParser<DtoGetterMeta>
        implements DtoGetterMetaParser {

    @Override
    protected DtoGetterMeta metaFor(VariableElement element) {
        return metaOf(element);
    }
}
