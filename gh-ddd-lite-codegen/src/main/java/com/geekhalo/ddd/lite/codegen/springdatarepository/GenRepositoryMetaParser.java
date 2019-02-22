package com.geekhalo.ddd.lite.codegen.springdatarepository;

import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.TypeCollector;
import com.geekhalo.ddd.lite.codegen.repository.Index;
import com.geekhalo.ddd.lite.codegen.repository.Indexes;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.getParentPacketName;
import static java.util.stream.Collectors.toMap;

public final class GenRepositoryMetaParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenRepositoryMetaParser.class);

    private final TypeCollector typeCollector;

    public GenRepositoryMetaParser(TypeCollector typeCollector) {
        this.typeCollector = typeCollector;
    }


    public GenRepositoryMeta parse(TypeElement typeElement, Annotation annotation){
        if (annotation instanceof GenSpringDataRepository){
            return parseFromSpringDataRepository(typeElement, (GenSpringDataRepository) annotation);
        }
        if (annotation instanceof EnableGenForAggregate){
            return parseFromEnableGenForAggregate(typeElement, (EnableGenForAggregate) annotation);
        }

        throw new IllegalArgumentException();
    }

    private GenRepositoryMeta parseFromEnableGenForAggregate(TypeElement typeElement, EnableGenForAggregate annotation) {
        TypeElement aggType = typeElement;
        String pkgName = getDefaultPkgName(typeElement);
        String clsName = getDefaultClsName(typeElement);
        List<GenRepositoryMeta.IndexMeta> indexMetas = getIndexMeta(aggType);
        return new GenRepositoryMeta(true, pkgName, clsName, typeElement, indexMetas);
    }

    private GenRepositoryMeta parseFromSpringDataRepository(TypeElement typeElement, GenSpringDataRepository springDataRepository) {
        TypeElement aggType = typeElement;
        String pkgName = getPkgName(typeElement, springDataRepository.pkgName());
        String clsName = getClsName(typeElement, springDataRepository.clsName());
        List<GenRepositoryMeta.IndexMeta> indexMetas = getIndexMeta(aggType);
        return new GenRepositoryMeta(springDataRepository.useQueryDsl(), pkgName, clsName, typeElement, indexMetas);
    }

    private List<GenRepositoryMeta.IndexMeta> getIndexMeta(TypeElement aggType) {
        List<Index> indices = findIndexes(aggType);
        Map<String, VariableElement> fieldMap = findAllFields(aggType).stream()
                .collect(toMap(variableElement-> variableElement.getSimpleName().toString(), variableElement->variableElement));

        List<GenRepositoryMeta.IndexMeta> indexMetas = Lists.newArrayList();
        for (Index index : indices){
            GenRepositoryMeta.IndexMeta indexMeta = new GenRepositoryMeta.IndexMeta(index.unique());
            for (String fieldName : index.value()){
                VariableElement variableElement = fieldMap.get(fieldName);
                if (variableElement != null){
                    indexMeta.addParam(variableElement);
                }else {
                    LOGGER.error("failed to find field {}", fieldName);
                    continue;
                }
            }
            indexMetas.add(indexMeta);
        }
        return indexMetas;
    }

    private List<Index> findIndexes(Element element) {
        List<Index> indices = Lists.newArrayList();
        Indexes indexes = element.getAnnotation(Indexes.class);
        if (indexes != null){
            indices.addAll(Arrays.asList(indexes.value()));
        }else {
            Index index = element.getAnnotation(Index.class);
            if (index != null){
                indices.add(index);
            }
        }
        return indices;
    }

    private String getPkgName(TypeElement typeElement, String pkgName) {
        if (StringUtils.isNotEmpty(pkgName)){
            return pkgName;
        }
        return getDefaultPkgName(typeElement);
    }

    private String getDefaultPkgName(TypeElement typeElement) {
        return typeElement.getEnclosingElement().toString();
    }

    private String getClsName(TypeElement typeElement, String clsName){
        if (StringUtils.isNotEmpty(clsName)){
            return clsName;
        }
        return getDefaultClsName(typeElement);
    }

    private String getDefaultClsName(TypeElement typeElement) {
        return "Base" + typeElement.getSimpleName().toString() + "Repository";
    }

    private Set<VariableElement> findAllFields(Element element) {
        Set<VariableElement> result = Sets.newHashSet();
        Element tmp = element;
        do {
            result.addAll(findFields(tmp));
            tmp = getSuperType(tmp);
        }while (tmp != null);
        return result;
    }

    private Element getSuperType(Element element) {
        if (element instanceof TypeElement){
            TypeMirror typeMirror = ((TypeElement)element).getSuperclass();
            if (typeMirror == null){
                return null;
            }
            return this.typeCollector.getByName(typeMirror.toString());
        }
        return null;
    }

    private Set<VariableElement> findFields(Element element) {
        return ElementFilter.fieldsIn(element.getEnclosedElements()).stream()
                .collect(Collectors.toSet());
    }
}
