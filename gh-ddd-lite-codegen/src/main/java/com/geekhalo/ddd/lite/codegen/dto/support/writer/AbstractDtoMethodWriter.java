package com.geekhalo.ddd.lite.codegen.dto.support.writer;

import com.geekhalo.ddd.lite.codegen.dto.GenDtoPropertyConvert;
import com.geekhalo.ddd.lite.codegen.dto.GenDtoPropertyModel;
import com.geekhalo.ddd.lite.codegen.dto.support.meta.DtoGetterMeta;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

abstract class AbstractDtoMethodWriter implements DtoMethodWriter {
    protected abstract boolean accept(DtoGetterMeta methodMeta);
    protected abstract void preWrite(TypeSpec.Builder typeBuilder, MethodSpec.Builder cMethodBuilder);
    protected abstract void write(TypeSpec.Builder builder, MethodSpec.Builder cMethodSpecBuilder, DtoGetterMeta methodMeta);
    protected abstract void postWrite(TypeSpec.Builder typeBuilder, MethodSpec.Builder cMethodBuilder);

    @Override
    public void write(TypeSpec.Builder builder, MethodSpec.Builder cMethodSpecBuilder, List<DtoGetterMeta> methodMetas) {
        List<DtoGetterMeta> metas = methodMetas.stream()
                .filter(genVoMethodMeta -> accept(genVoMethodMeta))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(metas)) {
            preWrite(builder, cMethodSpecBuilder);
            metas.forEach(genVoMethodMeta -> {
                write(builder, cMethodSpecBuilder, genVoMethodMeta);
                methodMetas.remove(genVoMethodMeta);
            });
            postWrite(builder, cMethodSpecBuilder);
        }
    }

    protected boolean checkByConfigAndType(DtoGetterMeta methodMeta, GenDtoPropertyModel model, String typeName){
        GenDtoPropertyConvert genDtoPropertyConvert = methodMeta.voPropertyConvert();
        if (genDtoPropertyConvert != null){
            if (genDtoPropertyConvert.value() == GenDtoPropertyModel.NONE){
                return false;
            }
            return genDtoPropertyConvert.value() == model;
        }
        return typeName.equals(methodMeta.type().toString());
    }
}
