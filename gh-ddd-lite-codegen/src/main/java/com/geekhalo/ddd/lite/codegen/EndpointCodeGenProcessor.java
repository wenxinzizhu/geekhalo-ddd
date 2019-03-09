package com.geekhalo.ddd.lite.codegen;

import com.geekhalo.ddd.lite.codegen.controller.GenControllerPlugin;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class EndpointCodeGenProcessor extends AbstractCodeGenProcessor {

    private final ProcessorPlugin[] plugins = new ProcessorPlugin[]{
            new GenControllerPlugin()
    };

    @Override
    ProcessorPlugin[] getPlugins() {
        return this.plugins;
    }
}
