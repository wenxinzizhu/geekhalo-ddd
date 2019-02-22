package com.geekhalo.ddd.lite.codegen.updater;

import com.geekhalo.ddd.lite.codegen.AbstractProcessorPlugin;
import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.EnableGenForEntity;
import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.updater.support.meta.UpdaterMeta;
import com.geekhalo.ddd.lite.codegen.updater.support.parser.UpdaterMetaParser;
import com.geekhalo.ddd.lite.codegen.updater.support.writer.UpdaterMethodWriter;
import com.squareup.javapoet.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

public class GenUpdaterPlugin
        extends AbstractProcessorPlugin {
    private final UpdaterMetaParser metaParser = new UpdaterMetaParser();
    private final UpdaterMethodWriter creatorMethodWriter = new UpdaterMethodWriter();


    @Override
    protected void process(TypeElement typeElement, Annotation annotation) {
        GenUpdaterParser parser = new GenUpdaterParser(typeElement);
        parser.read(annotation);
        String packageName = parser.getPackageName();
        String className = parser.getClassName();

        String parentClassName = parser.getParentClassName();

        TypeVariableName typeVariableName = TypeVariableName.get("T extends " + className);

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addTypeVariable(typeVariableName)
                .addAnnotation(Data.class)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        if (StringUtils.isNotEmpty(parentClassName)){
            ClassName parent = ClassName.bestGuess(parentClassName);
            TypeName typeName = TypeVariableName.get("T");
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(parent, typeName);
            typeSpecBuilder.superclass(parameterizedTypeName);
        }

        MethodSpec.Builder acceptMethodBuilder = MethodSpec.methodBuilder("accept")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(ParameterSpec.builder(TypeName.get(typeElement.asType()), "target")
                        .build());
        if (StringUtils.isNotEmpty(parentClassName)){
            acceptMethodBuilder.addStatement("super.accept(target)");
        }

        UpdaterMeta creatorMeta = this.metaParser.parse(typeElement);
        this.creatorMethodWriter.writeTo(typeSpecBuilder, acceptMethodBuilder, creatorMeta.getMethodMetas());
        typeSpecBuilder.addMethod(acceptMethodBuilder.build());

        getJavaSourceCollector().register(new JavaSource(packageName, className, typeSpecBuilder));
    }

    @Override
    public Class<Annotation>[] applyAnnCls() {
        return new Class[]{ GenUpdater.class, EnableGenForAggregate.class, EnableGenForEntity.class};
    }

    @Override
    public <A extends Annotation> Class<A>[] ignoreAnnCls() {
        return new Class[]{GenUpdaterIgnore.class};
    }
}
