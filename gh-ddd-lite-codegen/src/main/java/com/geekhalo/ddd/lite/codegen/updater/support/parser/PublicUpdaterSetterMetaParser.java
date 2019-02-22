package com.geekhalo.ddd.lite.codegen.updater.support.parser;

import com.geekhalo.ddd.lite.codegen.support.parser.PublicSetterMetaParser;
import com.geekhalo.ddd.lite.codegen.updater.support.meta.UpdaterSetterMeta;

import javax.lang.model.element.ExecutableElement;

final class PublicUpdaterSetterMetaParser
        extends PublicSetterMetaParser<UpdaterSetterMeta>
        implements UpdaterSetterMetaParser {

    @Override
    protected UpdaterSetterMeta metaFor(ExecutableElement executableElement) {
        return metaOf(executableElement);
    }
}
