package com.geekhalo.ddd.lite.codegen.controller;

import com.geekhalo.ddd.lite.codegen.AbstractProcessorPlugin;
import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.controller.parser.GenControllerMethodMetaParser;
import com.geekhalo.ddd.lite.codegen.controller.writer.GenControllerCreateMethodWriter;
import com.geekhalo.ddd.lite.codegen.controller.writer.GenControllerSelectMethodWriter;
import com.geekhalo.ddd.lite.codegen.controller.writer.GenControllerUpdateMethodWriter;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;


public final class GenControllerPlugin
    extends AbstractProcessorPlugin{


    @Override
    public Class<Annotation>[] applyAnnCls() {
        return new Class[]{GenController.class};
    }

    @Override
    protected void process(TypeElement typeElement, Annotation annotation) {
        GenControllerMethodMetaParser methodMetaParser = new GenControllerMethodMetaParser(getTypeCollector());
        GenControllerAnnotationParser parser = new GenControllerAnnotationParser(typeElement, (GenController) annotation);

        JavaSource support = getJavaSourceCollector().getByName(parser.getControllerName());
        if (support == null){
            TypeSpec.Builder supportBuilder =
                    new GenControllerSupportBuilderFactory(parser.getTypeElement(), parser.getSimpleEndpointName(), parser.getParentName())
                            .create();
            support = new JavaSource(parser.getPkgName(), parser.getSimpleEndpointName(), supportBuilder);
            getJavaSourceCollector().register(support);
        }

        GenControllerMethodMeta methodMeta = methodMetaParser.parse(parser.getTypeElement());

        new GenControllerCreateMethodWriter(parser, this::createJava,
                methodMeta.getCreateMethods(), this.getTypeCollector()).writeTo(support.getTypeSpecBuilder());
        new GenControllerUpdateMethodWriter(parser, this::createJava,
                methodMeta.getUpdateMethods(), this.getTypeCollector()).writeTo(support.getTypeSpecBuilder());
        new GenControllerSelectMethodWriter(parser, this::createJava,
                methodMeta.getQueryMethods(), getTypeCollector()).writeTo(support.getTypeSpecBuilder());

    }

    public void createJava(JavaSource javaSource){
        getJavaSourceCollector().register(javaSource.getFullName(),javaSource);
    }


    @Override
    public <A extends Annotation> Class<A>[] ignoreAnnCls() {
        return new Class[]{GenControllerIgnore.class};
    }
}
