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
    private final boolean genIfc;
    private final String superClassName;
    private final String ifcFullName;
//    private final TypeElement modelType;
//    private final TypeElement repositoryType;
    private final boolean createDefaultElement;

    public ApplicationSupportBuilderFactory(
            String className,
            boolean genIfc, String superClassName,
            String ifcFullName,
            boolean createDefaultElement) {
        this.className = className;
        this.genIfc = genIfc;
        this.superClassName = superClassName;
        this.ifcFullName = ifcFullName;
        this.createDefaultElement = createDefaultElement;
    }

    @Override
    public TypeSpec.Builder create() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.ABSTRACT)
                    .superclass(ClassName.bestGuess(superClassName));
        if (this.genIfc) {
                    builder.addSuperinterface(ClassName.bestGuess(ifcFullName));
        }
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

        applicationImplBuilder.addMethod(eventBusGetter.build());

    }
}
