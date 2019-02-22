package com.geekhalo.ddd.lite.codegen.creator.support.parser;

import com.geekhalo.ddd.lite.codegen.creator.GenCreatorIgnore;
import com.geekhalo.ddd.lite.codegen.creator.support.meta.CreatorSetterMeta;
import com.geekhalo.ddd.lite.codegen.support.parser.ModelMethodMetaParser;

import javax.lang.model.element.Element;

public interface CreatorSetterMetaParser extends ModelMethodMetaParser<CreatorSetterMeta> {

    default CreatorSetterMeta metaOf(Element element){
        CreatorSetterMeta meta = new CreatorSetterMeta();
        GenCreatorIgnore ignore = element.getAnnotation(GenCreatorIgnore.class);
        if (ignore != null){
            meta.setIgnore(true);
        }
       return meta;
    }
}
