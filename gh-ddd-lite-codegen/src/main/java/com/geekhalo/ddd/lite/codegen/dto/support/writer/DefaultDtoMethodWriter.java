package com.geekhalo.ddd.lite.codegen.dto.support.writer;

import com.geekhalo.ddd.lite.codegen.dto.support.meta.DtoGetterMeta;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.Modifier;

import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.getFieldName;

public final class DefaultDtoMethodWriter extends AbstractDtoMethodWriter {
    @Override
    protected boolean accept(DtoGetterMeta methodMeta) {
        return methodMeta.voPropertyConvert() == null;
    }

    @Override
    protected void preWrite(TypeSpec.Builder typeBuilder, MethodSpec.Builder cMethodBuilder) {

    }

    @Override
    protected void write(TypeSpec.Builder builder, MethodSpec.Builder cMethodSpecBuilder, DtoGetterMeta methodMeta) {
        FieldSpec fieldSpec = FieldSpec.builder(methodMeta.type(), methodMeta.name(), Modifier.PRIVATE)
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

        if (methodMeta.type().equals(TypeName.BOOLEAN)) {
            cMethodSpecBuilder.addStatement("this.set$L(source.is$L())", fieldName, fieldName);
        } else {
            cMethodSpecBuilder.addStatement("this.set$L(source.get$L())", fieldName, fieldName);
        }

    }

    @Override
    protected void postWrite(TypeSpec.Builder typeBuilder, MethodSpec.Builder cMethodBuilder) {

    }
}
