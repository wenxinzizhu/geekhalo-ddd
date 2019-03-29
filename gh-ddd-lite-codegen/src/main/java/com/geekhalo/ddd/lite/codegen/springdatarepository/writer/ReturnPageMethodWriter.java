package com.geekhalo.ddd.lite.codegen.springdatarepository.writer;

import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.springdatarepository.GenRepositoryMeta;
import com.geekhalo.ddd.lite.codegen.springdatarepository.ParamElement;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.squareup.javapoet.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

public class ReturnPageMethodWriter extends RepositoryMethodWriterSupport {


    public ReturnPageMethodWriter(GenRepositoryMeta repositoryMeta) {
        super(repositoryMeta);
    }

    @Override
    protected void writeQueryDslMethodTo(List<ParamElement> paramElements, JavaSource javaSource, boolean unique, boolean isFull) {
        if (isFull && unique){
            return;
        }

        String methodName = "findBy" + formatName(paramElements);
        {
            MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                    .returns(ParameterizedTypeName.get(ClassName.get(Page.class), TypeName.get(this.getRepositoryMeta().getAggType().asType())));

            paramElements.forEach(element -> {
                methodSpecBuilder.addParameter(element.getTypeName(), element.getName());
            });

            methodSpecBuilder.addParameter(ClassName.get(Predicate.class), "predicate");
            methodSpecBuilder.addParameter(ClassName.get(Pageable.class), "pageable");

            methodSpecBuilder.addStatement("$T booleanBuilder = new $T()", ClassName.get(BooleanBuilder.class), ClassName.get(BooleanBuilder.class));
            TypeElement aggElement = this.getRepositoryMeta().getAggType();
            String aggName = aggElement.getSimpleName().toString();
            paramElements.stream()
                    .filter(paramElement -> paramElement.isField() )
                    .forEach(element -> {
                        methodSpecBuilder.addStatement("booleanBuilder.and($T.$L.$L.eq($L));",
                                ClassName.bestGuess(aggElement.getEnclosingElement().toString() + ".Q" + aggElement.getSimpleName().toString()),
                                aggName.substring(0, 1).toLowerCase() + aggName.substring(1),
                                element.getName(),
                                element.getName());
                    });

            methodSpecBuilder.addStatement("booleanBuilder.and(predicate)");
            methodSpecBuilder.addStatement("return findAll(booleanBuilder.getValue(), pageable)",
                    ClassName.get(Lists.class));

            javaSource.addMethod(methodSpecBuilder.build());
        }
    }

    @Override
    protected void writeMethodTo(List<ParamElement> paramElements, JavaSource javaSource, boolean unique, boolean isFull) {
        if (isFull && unique){
            return;
        }
        String methodName = "findBy" + formatName(paramElements);
        {
            MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(ParameterizedTypeName.get(ClassName.get(Page.class), TypeName.get(this.getRepositoryMeta().getAggType().asType())));

            paramElements.forEach(element -> {
                methodSpecBuilder.addParameter(element.getTypeName(), element.getName());
            });
            methodSpecBuilder.addParameter(ClassName.get(Pageable.class), "pageable");
            javaSource.addMethod(methodSpecBuilder.build());
        }
    }
}
