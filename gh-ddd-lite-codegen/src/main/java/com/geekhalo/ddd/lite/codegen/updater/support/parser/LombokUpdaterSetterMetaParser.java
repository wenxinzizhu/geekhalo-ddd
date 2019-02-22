package com.geekhalo.ddd.lite.codegen.updater.support.parser;

import com.geekhalo.ddd.lite.codegen.support.parser.LombokSetterMetaParser;
import com.geekhalo.ddd.lite.codegen.updater.support.meta.UpdaterSetterMeta;

import javax.lang.model.element.VariableElement;

final class LombokUpdaterSetterMetaParser
        extends LombokSetterMetaParser<UpdaterSetterMeta>
        implements UpdaterSetterMetaParser {

    @Override
    protected UpdaterSetterMeta metaFor(VariableElement element) {
        return metaOf(element);
    }

}
