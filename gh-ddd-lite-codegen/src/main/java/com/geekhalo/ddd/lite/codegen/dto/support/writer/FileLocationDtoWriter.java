package com.geekhalo.ddd.lite.codegen.dto.support.writer;

import com.geekhalo.ddd.lite.codegen.dto.GenDtoPropertyModel;
import com.geekhalo.ddd.lite.codegen.dto.support.meta.DtoGetterMeta;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.Modifier;

import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.getFieldName;

public final class FileLocationDtoWriter extends AbstractDtoMethodWriter {
    @Override
    protected boolean accept(DtoGetterMeta methodMeta) {
        return checkByConfigAndType(methodMeta, GenDtoPropertyModel.TO_FILE_LOCATION_VO, "com.whkj.base.file.FileLocation");
    }

    @Override
    protected void preWrite(TypeSpec.Builder typeBuilder, MethodSpec.Builder cMethodBuilder) {
        cMethodBuilder.addParameter(ClassName.bestGuess("com.whkj.base.file.FileLocationRecover"), "fileLocationRecover");
    }

    @Override
    protected void write(TypeSpec.Builder builder, MethodSpec.Builder cMethodSpecBuilder, DtoGetterMeta methodMeta) {
        FieldSpec fieldSpec = FieldSpec.builder(ClassName.bestGuess("com.whkj.base.file.FileLocationVo"), methodMeta.name(), Modifier.PRIVATE)
                .addAnnotation(AnnotationSpec.builder(Setter.class)
                        .addMember("value", "$T.PACKAGE", AccessLevel.class)
                        .build())
                .addAnnotation(AnnotationSpec.builder(Getter.class)
                        .addMember("value", "$T.PUBLIC", AccessLevel.class)
                        .build())
                .addAnnotation(AnnotationSpec.builder(ApiModelProperty.class)
                        .addMember("value", "$S", methodMeta.description())
                        .addMember("name", "$S", methodMeta.name())
                        .build())
                .build();

        builder.addField(fieldSpec);

        String fieldName = getFieldName(methodMeta.name());

        cMethodSpecBuilder.addStatement("this.set$L(fileLocationRecover.getVo(source.get$L()))", fieldName, fieldName);
    }

    @Override
    protected void postWrite(TypeSpec.Builder typeBuilder, MethodSpec.Builder cMethodBuilder) {

    }
}
