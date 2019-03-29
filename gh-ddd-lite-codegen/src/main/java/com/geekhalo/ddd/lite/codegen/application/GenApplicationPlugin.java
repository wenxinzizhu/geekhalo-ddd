package com.geekhalo.ddd.lite.codegen.application;

import com.geekhalo.ddd.lite.codegen.AbstractProcessorPlugin;
import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.application.model.ModelBasedApplicationMethodWriter;
import com.geekhalo.ddd.lite.codegen.application.model.ModelBasedMethodMeta;
import com.geekhalo.ddd.lite.codegen.application.model.ModelBasedMethodMetaParser;
import com.geekhalo.ddd.lite.codegen.application.model.ModelBasedSupportMethodWriter;
import com.geekhalo.ddd.lite.codegen.application.repository.RepositoryBasedApplicationMethodWriter;
import com.geekhalo.ddd.lite.codegen.application.repository.RepositoryBasedMethodMeta;
import com.geekhalo.ddd.lite.codegen.application.repository.RepositoryBasedMethodMetaParser;
import com.geekhalo.ddd.lite.codegen.application.repository.RepositoryBasedSupportMethodWriter;
import com.geekhalo.ddd.lite.codegen.support.TypeBuilderFactory;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;


public final class GenApplicationPlugin
    extends AbstractProcessorPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenApplicationPlugin.class);
    private final ModelBasedMethodMetaParser modelBasedMethodMetaParser = new ModelBasedMethodMetaParser();
    private final RepositoryBasedMethodMetaParser repositoryBasedMethodMetaParser = new RepositoryBasedMethodMetaParser();

    @Override
    public Class<? extends Annotation>[] applyAnnCls() {
        return new Class[]{EnableGenForAggregate.class, GenApplication.class};
    }

    @Override
    protected void process(TypeElement typeElement, Annotation annotation) {
        if (annotation instanceof EnableGenForAggregate && typeElement.getModifiers().contains(Modifier.ABSTRACT)){
            return;
        }

        GenApplicationAnnotationParser parser = new GenApplicationAnnotationParser(typeElement);

        parser.read(annotation);


        String modelName = parser.getModelName();
        TypeElement modelType = getTypeCollector().getByName(modelName);
        if (modelType == null){
            LOGGER.error("failed to find model type {}.", modelName);
            return;
        }
        String modelDtoName = parser.getFullModelDtoName();
        TypeElement modelVoType = getTypeCollector().getByName(modelDtoName);
        if (parser.isRepository() && modelVoType == null){
            LOGGER.error("failed to find model type {}.", modelDtoName);
            return;
        }

        if (parser.isRepository()){
            RepositoryBasedMethodMeta methodMeta = this.repositoryBasedMethodMetaParser.parse(modelType, modelVoType, typeElement);
            if (parser.genIfc()){
                JavaSource ifcJavaSource = createIfcJavaSource(parser);
                RepositoryBasedApplicationMethodWriter writer = new RepositoryBasedApplicationMethodWriter(methodMeta);
                writer.writeTo(ifcJavaSource);
            }

            if (parser.genImp()){
                JavaSource impJavaSource = createSupportJavaSource(parser, modelType);
                RepositoryBasedSupportMethodWriter writer = new RepositoryBasedSupportMethodWriter(methodMeta);
                writer.writeTo(impJavaSource);
            }

        }else {
            ModelBasedMethodMeta modelBasedMethodMeta = this.modelBasedMethodMetaParser.parse(modelType, getTypeCollector());
            if (parser.genIfc()){
                JavaSource ifcJavaSource = createIfcJavaSource(parser);
                ModelBasedApplicationMethodWriter writer = new ModelBasedApplicationMethodWriter(modelBasedMethodMeta);
                writer.writeTo(ifcJavaSource);
            }
            if (parser.genImp()){
                JavaSource impJavaSource = createSupportJavaSource(parser, modelType);
                ModelBasedSupportMethodWriter writer = new ModelBasedSupportMethodWriter(modelBasedMethodMeta);
                writer.writeTo(impJavaSource);
            }

        }

    }

    private JavaSource createSupportJavaSource(GenApplicationAnnotationParser parser, TypeElement modelType) {
        JavaSource impJavaSource = getJavaSourceCollector().getByName(parser.getFullImplName());
        if (impJavaSource == null) {
            TypeBuilderFactory factory = new ApplicationSupportBuilderFactory(parser.getImpName(),
                    parser.genIfc(),
                    parser.getSuperClassName(),
                    parser.getFullIfcName(),
                    modelType,
                    getTypeCollector().getByName(parser.getFullRepositoryName()),
                    true);
            impJavaSource = new JavaSource(parser.getImpPkg(), parser.getImpName(), factory.create());
            getJavaSourceCollector().register(parser.getFullImplName(), impJavaSource);
        }
        return impJavaSource;

    }

    private JavaSource createIfcJavaSource(GenApplicationAnnotationParser parser){
        JavaSource ifcJavaSource = getJavaSourceCollector().getByName(parser.getFullIfcName());
        if (ifcJavaSource == null) {
            TypeSpec.Builder builder = new ApplicationBuilderFactory(parser.getIfcName()).create();
            ifcJavaSource = new JavaSource(parser.getPkgName(), parser.getIfcName(), builder);
            getJavaSourceCollector().register(parser.getFullIfcName(), ifcJavaSource);

        }
        return ifcJavaSource;
    }

    @Override
    public <A extends Annotation> Class<A>[] ignoreAnnCls() {
        return new Class[]{GenApplicationIgnore.class};
    }
}
