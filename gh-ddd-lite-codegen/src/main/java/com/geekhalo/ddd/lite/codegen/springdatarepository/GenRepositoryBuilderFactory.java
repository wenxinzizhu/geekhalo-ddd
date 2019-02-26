package com.geekhalo.ddd.lite.codegen.springdatarepository;

import com.geekhalo.ddd.lite.codegen.support.TypeBuilderFactory;
import com.geekhalo.ddd.lite.domain.AggregateRepository;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;


public final class GenRepositoryBuilderFactory implements TypeBuilderFactory {
    private final GenRepositoryMeta repositoryMeta;

    public GenRepositoryBuilderFactory(GenRepositoryMeta repositoryMeta) {
        this.repositoryMeta = repositoryMeta;
    }

    @Override
    public TypeSpec.Builder create() {
        TypeSpec.Builder typeBuilder = TypeSpec.interfaceBuilder(this.repositoryMeta.getClsName())
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(AggregateRepository.class),
                        repositoryMeta.getIdType(),
                        ClassName.get(repositoryMeta.getAggType().asType())
                )).addSuperinterface(ParameterizedTypeName.get(ClassName.get(Repository.class),
                        ClassName.get(repositoryMeta.getAggType().asType()),
                        repositoryMeta.getIdType()));
        if (this.repositoryMeta.isUseQueryDsl()){
            typeBuilder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(QuerydslPredicateExecutor.class),
                    ClassName.get(repositoryMeta.getAggType().asType())));
        }
        return typeBuilder;
    }
}
