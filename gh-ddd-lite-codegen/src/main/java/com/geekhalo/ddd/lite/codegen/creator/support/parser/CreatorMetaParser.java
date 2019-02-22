package com.geekhalo.ddd.lite.codegen.creator.support.parser;

import com.geekhalo.ddd.lite.codegen.creator.support.meta.CreatorMeta;
import com.geekhalo.ddd.lite.codegen.creator.support.meta.CreatorSetterMeta;
import com.geekhalo.ddd.lite.codegen.support.parser.ModelMetaParser;

import javax.lang.model.element.TypeElement;

public class CreatorMetaParser extends ModelMetaParser<CreatorSetterMeta, CreatorMeta, CreatorSetterMetaParser> {

    @Override
    protected CreatorMeta modelFor(TypeElement typeElement) {
        return new CreatorMeta(typeElement);
    }

    @Override
    protected CreatorSetterMetaParser[] parsers() {
        return new CreatorSetterMetaParser[]{
                new LombokCreatorSetterMetaParser(),
                new PublicCreatorSetterMetaParser()
        };
    }
}
