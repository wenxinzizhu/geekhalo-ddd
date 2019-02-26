package com.geekhalo.ddd.lite.codegen.dto.support.parser;

import com.geekhalo.ddd.lite.codegen.dto.GenDtoIgnore;
import com.geekhalo.ddd.lite.codegen.dto.GenDtoPropertyConvert;
import com.geekhalo.ddd.lite.codegen.dto.support.meta.DtoGetterMeta;
import com.geekhalo.ddd.lite.codegen.support.parser.ModelMethodMetaParser;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.List;

public interface DtoGetterMetaParser extends ModelMethodMetaParser<DtoGetterMeta> {
    @Override
    List<DtoGetterMeta> parse(TypeElement typeElement);

    default DtoGetterMeta metaOf(Element element){
        DtoGetterMeta dtoGetterMeta = new DtoGetterMeta();
        GenDtoIgnore voIgnore = element.getAnnotation(GenDtoIgnore.class);
        if (voIgnore != null){
            dtoGetterMeta.setIgnore(true);
        }
        GenDtoPropertyConvert genDtoPropertyConvert = element.getAnnotation(GenDtoPropertyConvert.class);
        if (genDtoPropertyConvert != null){
            dtoGetterMeta.setVoPropertyConvert(genDtoPropertyConvert);
        }
        return dtoGetterMeta;
    }
}
