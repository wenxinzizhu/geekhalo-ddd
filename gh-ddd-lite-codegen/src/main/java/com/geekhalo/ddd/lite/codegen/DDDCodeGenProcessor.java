package com.geekhalo.ddd.lite.codegen;

import com.geekhalo.ddd.lite.codegen.application.GenApplicationPlugin;
import com.geekhalo.ddd.lite.codegen.converter.GenCodeBasedEnumConverterPlugin;
import com.geekhalo.ddd.lite.codegen.creator.GenCreatorPlugin;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerPlugin;
import com.geekhalo.ddd.lite.codegen.springdatarepository.GenRepositoryPlugin;
import com.geekhalo.ddd.lite.codegen.updater.GenUpdaterPlugin;
import com.geekhalo.ddd.lite.codegen.dto.GenDtoPlugin;
import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DDDCodeGenProcessor<T extends Annotation> extends AbstractProcessor {
    private final TypeCollector typeCollector = new TypeCollector();
    private final ProcessorPlugin[] plugins = new ProcessorPlugin[]{
            new GenCodeBasedEnumConverterPlugin(),
            new GenDtoPlugin(),
            new GenCreatorPlugin(),
            new GenUpdaterPlugin(),
            new GenApplicationPlugin(),
            new GenControllerPlugin(),
            new GenRepositoryPlugin()
    };


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = Sets.newHashSet();
        for (ProcessorPlugin plugin : this.plugins){
            for (Class cls : plugin.applyAnnCls()) {
                types.add(cls.getName());
            }
        }
        return types;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        for (ProcessorPlugin plugin : this.plugins){
            plugin.init(this.typeCollector);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (CollectionUtils.isEmpty(annotations)){
            return true;
        }
        typeCollector.syncForm(roundEnv);
        for (ProcessorPlugin plugin : this.plugins) {
            try {
                System.out.println(String.format("process by plugin %s", plugin.getClass()));
                plugin.process(annotations, roundEnv);
                plugin.write(this.processingEnv.getFiler());
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return false;
    }


}
