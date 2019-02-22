package com.geekhalo.ddd.lite.codegen.creator.support.parser;

import com.geekhalo.ddd.lite.codegen.creator.support.meta.CreatorSetterMeta;
import com.geekhalo.ddd.lite.codegen.support.parser.LombokSetterMetaParser;

import javax.lang.model.element.VariableElement;

final class LombokCreatorSetterMetaParser
        extends LombokSetterMetaParser<CreatorSetterMeta>
        implements CreatorSetterMetaParser {

    @Override
    protected CreatorSetterMeta metaFor(VariableElement element) {
        return metaOf(element);
    }

}
