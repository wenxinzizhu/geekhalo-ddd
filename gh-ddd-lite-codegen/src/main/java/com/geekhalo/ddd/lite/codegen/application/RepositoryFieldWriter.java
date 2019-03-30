package com.geekhalo.ddd.lite.codegen.application;

import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.domain.AggregateRepository;
import com.squareup.javapoet.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.geekhalo.ddd.lite.codegen.application.Utils.getRepositoryFieldName;
import static com.geekhalo.ddd.lite.codegen.application.Utils.getRepositoryGetterName;

public class RepositoryFieldWriter {
    private final TypeElement modelType;
    private final TypeElement repositoryType;

    public RepositoryFieldWriter(TypeElement modelType,
                                 TypeElement repositoryType) {
        this.modelType = modelType;
        this.repositoryType = repositoryType;
    }

    public void writeTo(JavaSource javaSource){

        String repositoryGetterName = getRepositoryGetterName(this.modelType);
        String repositoryFieldName = getRepositoryFieldName(this.modelType);
        if (!javaSource.hasField(repositoryFieldName)){
            if (this.repositoryType != null) {
                FieldSpec field = FieldSpec.builder(TypeName.get(this.repositoryType.asType()), repositoryFieldName)
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(Autowired.class)
                        .build();
                javaSource.addField(field);

                MethodSpec.Builder repositoryGetter = MethodSpec.methodBuilder(repositoryGetterName)
                        .addModifiers(Modifier.PROTECTED)
                        .returns(TypeName.get(this.repositoryType.asType()))
                        .addStatement("return this.$L", repositoryFieldName);

                javaSource.addMethod(repositoryGetter.build());
            }else {
                MethodSpec.Builder repositoryGetter = MethodSpec.methodBuilder(repositoryGetterName)
                        .addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
                        .returns(ParameterizedTypeName.get(ClassName.get(AggregateRepository.class),
                                ClassName.get(Long.class),
                                ClassName.get(modelType.asType())
                        ));
                javaSource.addMethod(repositoryGetter.build());
            }
        }
    }
}
