package com.geekhalo.ddd.lite.codegen.dto;

import com.geekhalo.ddd.lite.codegen.AbstractProcessorPlugin;
import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.EnableGenForEntity;
import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.dto.support.meta.DtoGetterMeta;
import com.geekhalo.ddd.lite.codegen.dto.support.meta.DtoMeta;
import com.geekhalo.ddd.lite.codegen.dto.support.parser.DtoMetaParser;
import com.geekhalo.ddd.lite.codegen.dto.support.writer.*;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;

public class GenDtoPlugin extends AbstractProcessorPlugin {
    private final DtoMetaParser dtoMetaParser = new DtoMetaParser();
    private final DtoMethodWriter fileLocationVoWriter = new FileLocationDtoWriter();
    private final DtoMethodWriter instantToLongVoWriter = new InstantToLongDtoWriter();
    private final DtoMethodWriter selfDescribedEnumVoWriter = new SelfDescribedEnumDtoWriter();
    private final DtoMethodWriter defaultMethodWriter = new DefaultDtoMethodWriter();

    @Override
    protected void process(TypeElement element, Annotation annotation) {
        GenDtoParser parser = new GenDtoParser(element);
        parser.read(annotation);

        String className = parser.getClassName();

        String packageName = parser.getPackageName();

        String parentClassName = parser.getParentClassName();


        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addSuperinterface(TypeName.get(Serializable.class))
                .addAnnotation(Data.class)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        if (StringUtils.isNotEmpty(parentClassName)){
            ClassName parent = ClassName.bestGuess(parentClassName);
            typeSpecBuilder.superclass(parent);
        }


        MethodSpec.Builder cMethodSpecBuilder = MethodSpec.constructorBuilder()
                .addParameter(TypeName.get(element.asType()), "source")
                .addModifiers(Modifier.PUBLIC);

        if (StringUtils.isNotEmpty(parentClassName)){
            cMethodSpecBuilder.addStatement("super(source)");
        }

        DtoMeta dtoMeta = this.dtoMetaParser.parse(element);
        List<DtoGetterMeta> metas = dtoMeta.getMethodMetas();
        this.fileLocationVoWriter.write(typeSpecBuilder, cMethodSpecBuilder, metas);
        this.instantToLongVoWriter.write(typeSpecBuilder, cMethodSpecBuilder, metas);
        this.selfDescribedEnumVoWriter.write(typeSpecBuilder, cMethodSpecBuilder, metas);
        this.defaultMethodWriter.write(typeSpecBuilder, cMethodSpecBuilder, metas);

        typeSpecBuilder.addMethod(cMethodSpecBuilder.build());
        getJavaSourceCollector().register(new JavaSource(packageName, className, typeSpecBuilder));

    }

    @Override
    public Class<Annotation>[] applyAnnCls() {
        return new Class[]{GenDto.class, EnableGenForAggregate.class, EnableGenForEntity.class};
    }


    @Override
    public <A extends Annotation> Class<A>[] ignoreAnnCls() {
        return new Class[]{GenDtoIgnore.class};
    }
}
