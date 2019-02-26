package com.geekhalo.ddd.lite.codegen.application;

import com.geekhalo.ddd.lite.codegen.support.TypeBuilderFactory;
import com.geekhalo.ddd.lite.domain.AggregateRepository;
import com.geekhalo.ddd.lite.domain.DomainEventBus;
import com.squareup.javapoet.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.geekhalo.ddd.lite.codegen.application.Utils.getRepositoryFieldName;
import static com.geekhalo.ddd.lite.codegen.application.Utils.getRepositoryGetterName;

public final class ApplicationSupportBuilderFactory implements TypeBuilderFactory {
    private final String className;
    private final String superClassName;
    private final String ifcFullName;
    private final TypeElement modelType;
    private final TypeElement repositoryType;
    private final boolean createDefaultElement;

    public ApplicationSupportBuilderFactory(
                                            String className,
                                            String superClassName,
                                            String ifcFullName,
                                            TypeElement modelType,
                                            TypeElement repositoryType,
                                            boolean createDefaultElement) {
        this.className = className;
        this.superClassName = superClassName;
        this.ifcFullName = ifcFullName;
        this.modelType = modelType;
        this.repositoryType = repositoryType;
        this.createDefaultElement = createDefaultElement;
    }

    @Override
    public TypeSpec.Builder create() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.ABSTRACT)
                    .superclass(ClassName.bestGuess(superClassName))
                    .addSuperinterface(ClassName.bestGuess(ifcFullName));
        if (createDefaultElement){
            writeDefaultElements(builder);
        }
        return builder;
    }

    private void writeDefaultElements(TypeSpec.Builder applicationImplBuilder) {
        MethodSpec.Builder cMethodSpecBuilder = MethodSpec.constructorBuilder()
                .addParameter(ClassName.get(Logger.class), "logger")
                .addStatement("super(logger)")
                .addModifiers(Modifier.PROTECTED);
        applicationImplBuilder.addMethod(cMethodSpecBuilder.build());

        MethodSpec.Builder emptyMethodSpecBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED);
        applicationImplBuilder.addMethod(emptyMethodSpecBuilder.build());

        applicationImplBuilder.addField(FieldSpec.builder(ClassName.get(DomainEventBus.class), "domainEventBus")
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(Autowired.class)
                .build());

        MethodSpec.Builder eventBusGetter = MethodSpec.methodBuilder("getDomainEventBus")
                .addModifiers(Modifier.PROTECTED)
                .returns(ClassName.get(DomainEventBus.class))
                .addStatement("return this.domainEventBus");


        MethodSpec.Builder repositorySetter = null;
        String repositoryGetterName = getRepositoryGetterName(this.modelType);
        String repositoryFieldName = getRepositoryFieldName(this.modelType);
        if (this.repositoryType != null) {
            applicationImplBuilder.addField(FieldSpec.builder(TypeName.get(this.repositoryType.asType()), repositoryFieldName)
                    .addModifiers(Modifier.PRIVATE)
                    .addAnnotation(Autowired.class)
                    .build());
            repositorySetter = MethodSpec.methodBuilder(repositoryGetterName)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(TypeName.get(this.repositoryType.asType()))
                    .addStatement("return this.$L", repositoryFieldName);
        }else {
            repositorySetter = MethodSpec.methodBuilder(repositoryGetterName)
                    .addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
                    .returns(ParameterizedTypeName.get(ClassName.get(AggregateRepository.class),
                            ClassName.get(Long.class),
                            ClassName.get(modelType.asType())
                    ));
        }
        applicationImplBuilder.addMethod(repositorySetter.build());

        applicationImplBuilder.addMethod(eventBusGetter.build());

    }
}
