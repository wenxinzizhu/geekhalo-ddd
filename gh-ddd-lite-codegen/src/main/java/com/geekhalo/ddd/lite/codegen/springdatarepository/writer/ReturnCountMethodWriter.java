package com.geekhalo.ddd.lite.codegen.springdatarepository.writer;

import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.springdatarepository.GenRepositoryMeta;
import com.geekhalo.ddd.lite.codegen.springdatarepository.ParamElement;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

public class ReturnCountMethodWriter
        extends RepositoryMethodWriterSupport {

    public ReturnCountMethodWriter(GenRepositoryMeta repositoryMeta) {
        super(repositoryMeta);
    }

    @Override
    protected void writeQueryDslMethodTo(List<ParamElement> paramElements, JavaSource javaSource, boolean unique, boolean isFull) {
        if (isFull && unique){
            return;
        }
        String methodName = "countBy" + formatName(paramElements);
        {
            MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                    .returns(ClassName.get(Long.class));

            paramElements.forEach(element -> {
                methodSpecBuilder.addParameter(element.getTypeName(), element.getName());
            });

            methodSpecBuilder.addParameter(ClassName.get(Predicate.class), "predicate");

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
            methodSpecBuilder.addStatement("return this.count(booleanBuilder.getValue())",
                    ClassName.get(Lists.class));

            javaSource.addMethod(methodSpecBuilder.build());
        }
    }

    @Override
    protected void writeMethodTo(List<ParamElement> paramElements, JavaSource javaSource, boolean unique, boolean isFull) {
        if (isFull && unique){
            return;
        }

        String methodName = "countBy" + formatName(paramElements);
        {
            MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(ClassName.get(Long.class));

            paramElements.forEach(element -> {
                methodSpecBuilder.addParameter(element.getTypeName(), element.getName());
            });
            javaSource.addMethod(methodSpecBuilder.build());
        }
    }

}
