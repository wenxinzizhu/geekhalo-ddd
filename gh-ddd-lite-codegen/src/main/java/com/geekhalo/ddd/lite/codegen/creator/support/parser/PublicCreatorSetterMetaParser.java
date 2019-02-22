package com.geekhalo.ddd.lite.codegen.creator.support.parser;

import com.geekhalo.ddd.lite.codegen.creator.support.meta.CreatorSetterMeta;
import com.geekhalo.ddd.lite.codegen.support.parser.PublicSetterMetaParser;

import javax.lang.model.element.ExecutableElement;

final class PublicCreatorSetterMetaParser
        extends PublicSetterMetaParser<CreatorSetterMeta>
        implements CreatorSetterMetaParser {

    @Override
    protected CreatorSetterMeta metaFor(ExecutableElement executableElement) {
        return metaOf(executableElement);
    }
}
