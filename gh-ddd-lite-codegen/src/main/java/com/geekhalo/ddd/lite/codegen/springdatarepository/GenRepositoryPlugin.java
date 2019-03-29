package com.geekhalo.ddd.lite.codegen.springdatarepository;

import com.geekhalo.ddd.lite.codegen.AbstractProcessorPlugin;
import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.springdatarepository.writer.ReturnCountMethodWriter;
import com.geekhalo.ddd.lite.codegen.springdatarepository.writer.ReturnListMethodWriter;
import com.geekhalo.ddd.lite.codegen.springdatarepository.writer.ReturnOptinalMethodWriter;
import com.geekhalo.ddd.lite.codegen.springdatarepository.writer.ReturnPageMethodWriter;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

public class GenRepositoryPlugin extends AbstractProcessorPlugin {

    @Override
    protected void process(TypeElement typeElement, Annotation t) {
        if (t instanceof EnableGenForAggregate && typeElement.getModifiers().contains(Modifier.ABSTRACT)){
            return;
        }
        GenRepositoryMetaParser parser = new GenRepositoryMetaParser(getTypeCollector());
        GenRepositoryMeta genRepositoryMeta = parser.parse(typeElement, t);

        GenRepositoryBuilderFactory builderFactory = new GenRepositoryBuilderFactory(genRepositoryMeta);
        TypeSpec.Builder repositoryBuilder =  builderFactory.create();

        JavaSource javaSource = new JavaSource(genRepositoryMeta.getPkgName(), genRepositoryMeta.getClsName(), repositoryBuilder);
        getJavaSourceCollector().register(javaSource);

        new ReturnCountMethodWriter(genRepositoryMeta).writeTo(javaSource);
        new ReturnListMethodWriter(genRepositoryMeta).writeTo(javaSource);
        new ReturnPageMethodWriter(genRepositoryMeta).writeTo(javaSource);
        new ReturnOptinalMethodWriter(genRepositoryMeta).writeTo(javaSource);


    }

    @Override
    public <A extends Annotation> Class<A>[] applyAnnCls() {
        return new Class[]{GenSpringDataRepository.class, EnableGenForAggregate.class};
    }

    @Override
    public <A extends Annotation> Class<A>[] ignoreAnnCls() {
        return new Class[0];
    }
}
