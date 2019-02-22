package com.geekhalo.ddd.lite.codegen.creator.support.writer;

import com.geekhalo.ddd.lite.codegen.creator.support.meta.CreatorSetterMeta;
import com.geekhalo.ddd.lite.codegen.utils.TypeUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.Modifier;
import java.util.List;

public class JavaBeanBasedCreatorMethodWriter implements CreatorWriter{

    @Override
    public void writeTo(TypeSpec.Builder builder, MethodSpec.Builder acceptMethodBuilder, List<CreatorSetterMeta> setterMetas) {
        for (CreatorSetterMeta setterMeta : setterMetas) {
            String fieldName = TypeUtils.getFieldName(setterMeta.name());
            String targetSetterName = "set" + fieldName;
            String getterName = "get" + fieldName;
            acceptMethodBuilder.addStatement("target.$L($L())", targetSetterName, getterName);

//            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(DataOptional.class), setterMeta.type());
            FieldSpec fieldSpec = FieldSpec.builder(setterMeta.type(), setterMeta.name(), Modifier.PRIVATE)
                    .addAnnotation(AnnotationSpec.builder(Setter.class)
                            .addMember("value", "$T.PUBLIC", AccessLevel.class)
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

//            MethodSpec methodSpec = MethodSpec.methodBuilder(setterMeta.name())
//                    .addModifiers(Modifier.PUBLIC)
//                    .returns(TypeVariableName.get("T"))
//                    .addParameter(setterMeta.type(), setterMeta.name())
//                    .addStatement("this.$L = DataOptional.of($L)", setterMeta.name(), setterMeta.name())
//                    .addStatement("return (T) this")
//                    .build();
//            builder.addMethod(methodSpec);


//            ParameterizedTypeName consumerTypeName = ParameterizedTypeName.get(ClassName.get(Consumer.class), setterMeta.type());
//            MethodSpec applyMethodSpec = MethodSpec.methodBuilder(acceptMethodName)
//                    .addModifiers(Modifier.PUBLIC)
//                    .returns(TypeVariableName.get("T"))
//                    .addParameter(consumerTypeName, "consumer")
//                    .addCode(CodeBlock.builder()
//                            .add("if(this.$L != null){ \n", setterMeta.name())
//                            .add("\tconsumer.accept(this.$L.getValue());\n", setterMeta.name())
//                            .add("}\n")
//                            .build())
//                    .addStatement("return (T) this")
//                    .build();
//            builder.addMethod(applyMethodSpec);
        }
    }
}
