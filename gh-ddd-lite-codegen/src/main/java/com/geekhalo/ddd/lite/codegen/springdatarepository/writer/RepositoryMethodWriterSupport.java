package com.geekhalo.ddd.lite.codegen.springdatarepository.writer;

import com.geekhalo.ddd.lite.codegen.springdatarepository.GenRepositoryMeta;
import com.geekhalo.ddd.lite.codegen.springdatarepository.ParamElement;
import com.geekhalo.ddd.lite.codegen.support.MethodWriter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.squareup.javapoet.TypeSpec;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

abstract class RepositoryMethodWriterSupport implements MethodWriter {
    @Getter(AccessLevel.PROTECTED)
    private final GenRepositoryMeta repositoryMeta;
    private final Set<String> createdMethods = Sets.newHashSet();

    public RepositoryMethodWriterSupport(GenRepositoryMeta repositoryMeta) {
        this.repositoryMeta = repositoryMeta;
    }

    @Override
    public void writeTo(TypeSpec.Builder builder) {
        for (GenRepositoryMeta.IndexMeta indexMeta : repositoryMeta.getIndices()){
            for (int i = 0; i< indexMeta.getParams().size(); i++){
                boolean isFull = i == indexMeta.getParams().size() - 1;
                List<ParamElement> paramElements = Lists.newArrayList();
                for (int j=0; j<=i;j++){
                    paramElements.add(indexMeta.getParams().get(j));
                }

                String methodKey = createMethodKey(paramElements);
                if (createdMethods.contains(methodKey)){
                    continue;
                }else {
                    createdMethods.add(methodKey);
                }

                writeMethodTo(paramElements, builder, indexMeta.isUnique(), isFull);
                if (this.repositoryMeta.isUseQueryDsl()){
                    writeQueryDslMethodTo(paramElements, builder, indexMeta.isUnique(), isFull);
                }
            }
        }
    }

    private String createMethodKey(List<ParamElement> paramElements) {
        return formatName(paramElements);
    }

    protected abstract void writeQueryDslMethodTo(List<ParamElement> paramElements, TypeSpec.Builder builder, boolean unique, boolean isFull);

    protected abstract void writeMethodTo(List<ParamElement> paramElements, TypeSpec.Builder builder, boolean unique, boolean isFull);

    protected String formatName(List<ParamElement> elements) {
        return elements.stream()
                .filter(paramElement -> paramElement.isField() == true)
                .map(element -> element.getName())
                .map(name-> name.substring(0, 1).toUpperCase() + name.substring(1))
                .collect(Collectors.joining("And"));
    }
}
