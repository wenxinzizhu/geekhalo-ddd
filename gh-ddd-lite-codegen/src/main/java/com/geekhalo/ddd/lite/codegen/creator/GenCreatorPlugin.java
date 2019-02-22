package com.geekhalo.ddd.lite.codegen.creator;

import com.geekhalo.ddd.lite.codegen.AbstractProcessorPlugin;
import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.EnableGenForEntity;
import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.creator.support.meta.CreatorMeta;
import com.geekhalo.ddd.lite.codegen.creator.support.parser.CreatorMetaParser;
import com.geekhalo.ddd.lite.codegen.creator.support.writer.CreatorWriter;
import com.geekhalo.ddd.lite.codegen.creator.support.writer.DataOptionalBasedCreatorMethodWriter;
import com.geekhalo.ddd.lite.codegen.creator.support.writer.JavaBeanBasedCreatorMethodWriter;
import com.squareup.javapoet.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

public class GenCreatorPlugin
        extends AbstractProcessorPlugin {
    private final CreatorMetaParser metaParser = new CreatorMetaParser();
//    private final DataOptionalBasedCreatorMethodWriter dataOptionalBasedCreatorMethodWriter = new DataOptionalBasedCreatorMethodWriter();


    @Override
    protected void process(TypeElement typeElement, Annotation annotation) {
        GenCreatorParser parser = new GenCreatorParser(typeElement);
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

        CreatorMeta creatorMeta = this.metaParser.parse(typeElement);
        this.getCreatorWriter(parser.getType())
                .writeTo(typeSpecBuilder, acceptMethodBuilder, creatorMeta.getMethodMetas());
        typeSpecBuilder.addMethod(acceptMethodBuilder.build());

        getJavaSourceCollector().register( new JavaSource(packageName,className, typeSpecBuilder));
    }

    private CreatorWriter getCreatorWriter(GenCreatorType type){
        switch (type){
            case DATA_OPTION:
                return new DataOptionalBasedCreatorMethodWriter();
            case JAVA_BEAN:
                return new JavaBeanBasedCreatorMethodWriter();
        }
        return new JavaBeanBasedCreatorMethodWriter();
    }

    @Override
    public Class<Annotation>[] applyAnnCls() {
        return new Class[]{GenCreator.class, EnableGenForAggregate.class, EnableGenForEntity.class};
    }

    @Override
    public <A extends Annotation> Class<A>[] ignoreAnnCls() {
        return new Class[]{GenCreatorIgnore.class};
    }
}
