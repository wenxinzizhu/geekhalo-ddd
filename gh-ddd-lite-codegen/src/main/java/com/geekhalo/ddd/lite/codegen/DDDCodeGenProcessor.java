package com.geekhalo.ddd.lite.codegen;

import com.geekhalo.ddd.lite.codegen.application.GenApplicationPlugin;
import com.geekhalo.ddd.lite.codegen.converter.GenCodeBasedEnumConverterPlugin;
import com.geekhalo.ddd.lite.codegen.creator.GenCreatorPlugin;
import com.geekhalo.ddd.lite.codegen.dto.GenDtoPlugin;
import com.geekhalo.ddd.lite.codegen.springdatarepository.GenRepositoryPlugin;
import com.geekhalo.ddd.lite.codegen.updater.GenUpdaterPlugin;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DDDCodeGenProcessor extends AbstractCodeGenProcessor {

    private final ProcessorPlugin[] plugins = new ProcessorPlugin[]{
            new GenCodeBasedEnumConverterPlugin(),
            new GenDtoPlugin(),
            new GenCreatorPlugin(),
            new GenUpdaterPlugin(),
            new GenApplicationPlugin(),
            new GenRepositoryPlugin()
    };

    @Override
    ProcessorPlugin[] getPlugins() {
        return this.plugins;
    }

}
