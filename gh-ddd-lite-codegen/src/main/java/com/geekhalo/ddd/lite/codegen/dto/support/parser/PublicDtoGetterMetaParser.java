package com.geekhalo.ddd.lite.codegen.dto.support.parser;

import com.geekhalo.ddd.lite.codegen.support.parser.PublicGetterMetaParser;
import com.geekhalo.ddd.lite.codegen.dto.support.meta.DtoGetterMeta;

import javax.lang.model.element.ExecutableElement;

final class PublicDtoGetterMetaParser
        extends PublicGetterMetaParser<DtoGetterMeta>
        implements DtoGetterMetaParser {

    @Override
    protected DtoGetterMeta metaFor(ExecutableElement executableElement) {
        return metaOf(executableElement);
    }

}
