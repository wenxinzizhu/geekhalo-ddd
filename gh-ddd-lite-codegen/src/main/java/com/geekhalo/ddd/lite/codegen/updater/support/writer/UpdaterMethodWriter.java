package com.geekhalo.ddd.lite.codegen.updater.support.writer;

import com.geekhalo.ddd.lite.codegen.updater.support.meta.UpdaterSetterMeta;
import com.geekhalo.ddd.lite.codegen.utils.TypeUtils;
import com.geekhalo.ddd.lite.domain.support.DataOptional;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Consumer;

public class UpdaterMethodWriter {

    public void writeTo(TypeSpec.Builder builder, MethodSpec.Builder acceptMethodBuilder, List<UpdaterSetterMeta> setterMetas) {
        for (UpdaterSetterMeta setterMeta : setterMetas) {
            String fieldName = TypeUtils.getFieldName(setterMeta.name());
            String acceptMethodName = "accept" + fieldName;
            String targetSetterName = "set" + fieldName;
            acceptMethodBuilder.addStatement("this.$L(target::$L)", acceptMethodName, targetSetterName);

            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(DataOptional.class), setterMeta.type());
            FieldSpec fieldSpec = FieldSpec.builder(parameterizedTypeName, setterMeta.name(), Modifier.PRIVATE)
                    .addAnnotation(AnnotationSpec.builder(Setter.class)
                            .addMember("value", "$T.PRIVATE", AccessLevel.class)
                            .build())
                    .addAnnotation(AnnotationSpec.builder(Getter.class)
                            .addMember("value", "$T.PUBLIC", AccessLevel.class)
                            .build())
                    .addAnnotation(AnnotationSpec.builder(ApiModelProperty.class)
                            .addMember("value", "$S", setterMeta.description())
                            .addMember("name", "$S", setterMeta.name())
                            .build())
                    .build();
            builder.addField(fieldSpec);

            MethodSpec methodSpec = MethodSpec.methodBuilder(setterMeta.name())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeVariableName.get("T"))
                    .addParameter(setterMeta.type(), setterMeta.name())
                    .addStatement("this.$L = DataOptional.of($L)", setterMeta.name(), setterMeta.name())
                    .addStatement("return (T) this")
                    .build();
            builder.addMethod(methodSpec);


            ParameterizedTypeName consumerTypeName = ParameterizedTypeName.get(ClassName.get(Consumer.class), setterMeta.type());
            MethodSpec applyMethodSpec = MethodSpec.methodBuilder(acceptMethodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeVariableName.get("T"))
                    .addParameter(consumerTypeName, "consumer")
                    .addCode(CodeBlock.builder()
                            .add("if(this.$L != null){ \n", setterMeta.name())
                            .add("\tconsumer.accept(this.$L.getValue());\n", setterMeta.name())
                            .add("}\n")
                            .build())
                    .addStatement("return (T) this")
                    .build();
            builder.addMethod(applyMethodSpec);
        }
    }
}
