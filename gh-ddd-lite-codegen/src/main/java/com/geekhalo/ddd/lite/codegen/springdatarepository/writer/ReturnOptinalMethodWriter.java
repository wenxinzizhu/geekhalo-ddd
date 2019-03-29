package com.geekhalo.ddd.lite.codegen.springdatarepository.writer;

import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.springdatarepository.GenRepositoryMeta;
import com.geekhalo.ddd.lite.codegen.springdatarepository.ParamElement;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Optional;

public class ReturnOptinalMethodWriter extends RepositoryMethodWriterSupport{
    public ReturnOptinalMethodWriter(GenRepositoryMeta repositoryMeta) {
        super(repositoryMeta);
    }

    @Override
    protected void writeQueryDslMethodTo(List<ParamElement> paramElements, JavaSource javaSource, boolean unique, boolean isFull) {

    }

    @Override
    protected void writeMethodTo(List<ParamElement> paramElements, JavaSource javaSource, boolean unique, boolean isFull) {
        if (isFull && unique) {
            String methodName = "getBy" + formatName(paramElements);

            MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(ParameterizedTypeName.get(ClassName.get(Optional.class), TypeName.get(getRepositoryMeta().getAggType().asType())));

            paramElements.forEach(element -> {
                methodSpecBuilder.addParameter(element.getTypeName(), element.getName());
            });

            javaSource.addMethod(methodSpecBuilder.build());
        }
    }
}
