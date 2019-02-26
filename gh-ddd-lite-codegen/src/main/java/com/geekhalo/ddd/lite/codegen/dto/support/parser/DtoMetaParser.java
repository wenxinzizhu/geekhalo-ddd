package com.geekhalo.ddd.lite.codegen.dto.support.parser;

import com.geekhalo.ddd.lite.codegen.dto.support.meta.DtoGetterMeta;
import com.geekhalo.ddd.lite.codegen.dto.support.meta.DtoMeta;
import com.geekhalo.ddd.lite.codegen.support.parser.ModelMetaParser;

import javax.lang.model.element.TypeElement;

public class DtoMetaParser extends ModelMetaParser<DtoGetterMeta, DtoMeta, DtoGetterMetaParser> {
    @Override
    protected DtoMeta modelFor(TypeElement typeElement) {
        return new DtoMeta(typeElement);
    }

    @Override
    protected DtoGetterMetaParser[] parsers() {
        return new DtoGetterMetaParser[]{
                new LombokDtoGetterMetaParser(),
                new PublicDtoGetterMetaParser()

        };
    }
}
