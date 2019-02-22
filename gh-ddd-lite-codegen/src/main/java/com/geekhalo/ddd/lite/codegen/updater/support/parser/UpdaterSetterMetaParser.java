package com.geekhalo.ddd.lite.codegen.updater.support.parser;

import com.geekhalo.ddd.lite.codegen.support.parser.ModelMethodMetaParser;
import com.geekhalo.ddd.lite.codegen.updater.GenUpdaterIgnore;
import com.geekhalo.ddd.lite.codegen.updater.support.meta.UpdaterSetterMeta;

import javax.lang.model.element.Element;

public interface UpdaterSetterMetaParser extends ModelMethodMetaParser<UpdaterSetterMeta> {

    default UpdaterSetterMeta metaOf(Element element){
        UpdaterSetterMeta meta = new UpdaterSetterMeta();
        GenUpdaterIgnore ignore = element.getAnnotation(GenUpdaterIgnore.class);
        if (ignore != null){
            meta.setIgnore(true);
        }
       return meta;
    }
}
