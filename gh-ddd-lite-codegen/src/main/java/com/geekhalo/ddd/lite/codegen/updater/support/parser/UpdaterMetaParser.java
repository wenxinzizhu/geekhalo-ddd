package com.geekhalo.ddd.lite.codegen.updater.support.parser;

import com.geekhalo.ddd.lite.codegen.support.parser.ModelMetaParser;
import com.geekhalo.ddd.lite.codegen.updater.support.meta.UpdaterMeta;
import com.geekhalo.ddd.lite.codegen.updater.support.meta.UpdaterSetterMeta;

import javax.lang.model.element.TypeElement;

public final class UpdaterMetaParser
        extends ModelMetaParser<UpdaterSetterMeta, UpdaterMeta, UpdaterSetterMetaParser> {

    @Override
    protected UpdaterMeta modelFor(TypeElement typeElement) {
        return new UpdaterMeta(typeElement);
    }

    @Override
    protected UpdaterSetterMetaParser[] parsers() {
        return new UpdaterSetterMetaParser[]{
                new LombokUpdaterSetterMetaParser(),
                new PublicUpdaterSetterMetaParser()
        };
    }
}
