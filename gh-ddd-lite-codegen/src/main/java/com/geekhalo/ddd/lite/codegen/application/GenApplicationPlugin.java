package com.geekhalo.ddd.lite.codegen.application;

import com.geekhalo.ddd.lite.codegen.AbstractProcessorPlugin;
import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.application.model.ModelBasedApplictionMethodWriter;
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
        GenApplicationAnnotationParser genApplicationAnnotationParser = new GenApplicationAnnotationParser(typeElement);

        genApplicationAnnotationParser.read(annotation);

        String name = typeElement.getQualifiedName().toString();
        boolean isRepository = genApplicationAnnotationParser.isRepository();
        String modelName = genApplicationAnnotationParser.getModelName();


        TypeElement modelType = getTypeCollector().getByName(modelName);
        if (modelType == null){
            LOGGER.error("failed to find model type {}.", modelName);
            return;
//            throw new IllegalArgumentException(
//                    String.format("can not find model type %s", modelName)
//            );
        }
        String modelVo = genApplicationAnnotationParser.getFullModelDtoName();
        TypeElement modelVoType = getTypeCollector().getByName(modelVo);
        if (isRepository && modelVoType == null){
            LOGGER.error("failed to find model type {}.", modelVo);
            return;
//            throw new IllegalArgumentException(
//                    String.format("can not find model type %s", modelVo)
//            );
        }
        String pkgName = genApplicationAnnotationParser.getPkgName();
        String ifcName = genApplicationAnnotationParser.getIfcName();
        String ifcFullName = genApplicationAnnotationParser.getFullIfcName();

        String impName = genApplicationAnnotationParser.getImpName();
        String impFullName = genApplicationAnnotationParser.getFullImplName();

        JavaSource ifcJavaSource = getJavaSourceCollector().getByName(ifcFullName);
        if (ifcJavaSource == null){
            TypeSpec.Builder builder = new ApplicationBuilderFactory(ifcName).create();
            ifcJavaSource = new JavaSource(pkgName, ifcName, builder);
            getJavaSourceCollector().register(ifcFullName, ifcJavaSource);
        }

        JavaSource impJavaSource = null;
        if (genApplicationAnnotationParser.genImp()) {
            impJavaSource = getJavaSourceCollector().getByName(impFullName);
            if (impJavaSource == null) {
                TypeBuilderFactory factory = new ApplicationSupportBuilderFactory(impName,
                        genApplicationAnnotationParser.getSuperClassName(),
                        ifcFullName,
                        modelType,
                        getTypeCollector().getByName(genApplicationAnnotationParser.getFullRepositoryName()),
                        true);
                impJavaSource = new JavaSource(genApplicationAnnotationParser.getImpPkg(), impName, factory.create());
                getJavaSourceCollector().register(impFullName, impJavaSource);
            }
        }

        if (!isRepository){
            ModelBasedMethodMeta modelBasedMethodMeta = this.modelBasedMethodMetaParser.parse(modelType, getTypeCollector());
            {
                ModelBasedApplictionMethodWriter writer = new ModelBasedApplictionMethodWriter(modelBasedMethodMeta);
                writer.writeTo(ifcJavaSource.getTypeSpecBuilder());
            }
            {
                ModelBasedSupportMethodWriter writer = new ModelBasedSupportMethodWriter(modelBasedMethodMeta);
                writer.writeTo(impJavaSource.getTypeSpecBuilder());
            }
        }else {
            RepositoryBasedMethodMeta methodMeta = this.repositoryBasedMethodMetaParser.parse(modelType, modelVoType, typeElement);
            {
                RepositoryBasedApplicationMethodWriter writer = new RepositoryBasedApplicationMethodWriter(methodMeta);
                writer.writeTo(ifcJavaSource.getTypeSpecBuilder());
            }

            {
                RepositoryBasedSupportMethodWriter writer = new RepositoryBasedSupportMethodWriter(methodMeta);
                writer.writeTo(impJavaSource.getTypeSpecBuilder());
            }

        }

    }


    @Override
    public <A extends Annotation> Class<A>[] ignoreAnnCls() {
        return new Class[]{GenApplicationIgnore.class};
    }
}
