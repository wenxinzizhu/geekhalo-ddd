package com.geekhalo.ddd.lite.codegen.converter;

import com.geekhalo.ddd.lite.codegen.AbstractProcessorPlugin;
import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.lang.annotation.Annotation;


public final class GenCodeBasedEnumConverterPlugin
        extends AbstractProcessorPlugin {

    @Override
    protected void process(TypeElement typeElement, Annotation annotation) {
        GenCodeBasedEnumConverter genCodeBasedEnumConverter = (GenCodeBasedEnumConverter) annotation;
        String pkg = createPkgName(typeElement, genCodeBasedEnumConverter.pkgName());
        String className = createClassName(typeElement);

        TypeSpec.Builder typeSpecBuilder = createTypeBuilder(typeElement, className);

        typeSpecBuilder.addMethod(createConvertToDatabaseMethod(typeElement));
        typeSpecBuilder.addMethod(createConvertToEntityMethod(typeElement));

        getJavaSourceCollector().register(new JavaSource(pkg, className, typeSpecBuilder));
    }

    private MethodSpec createConvertToEntityMethod(TypeElement typeElement) {
        return MethodSpec.methodBuilder("convertToEntityAttribute")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeName.INT.box(), "i")
                    .returns(TypeName.get(typeElement.asType()))
                    .addStatement("if (i == null) return null")
                    .addCode(CodeBlock.builder()
                            .add("for ($T value : $T.values()){\n", TypeName.get(typeElement.asType()), TypeName.get(typeElement.asType()))
                            .add("\tif (value.getCode() == i){\n")
                            .add("\t\treturn value; \n")
                            .add("\t}\n")
                            .add("}\n")
                            .add("return null;\n")
                            .build())
                    .build();
    }

    private MethodSpec createConvertToDatabaseMethod(TypeElement typeElement) {
        return MethodSpec.methodBuilder("convertToDatabaseColumn")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeName.get(typeElement.asType()), "i")
                    .returns(TypeName.INT.box())
                    .addStatement("return i == null ? null : i.getCode()")
                    .build();
    }

    private TypeSpec.Builder createTypeBuilder(TypeElement typeElement, String className) {
        ParameterizedTypeName attributeConverter = ParameterizedTypeName.get(
                ClassName.get(AttributeConverter.class),
                TypeName.get(typeElement.asType()), TypeName.INT.box());

        return TypeSpec.classBuilder(className)
                .addSuperinterface(attributeConverter)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(AnnotationSpec.builder(Converter.class)
                        .addMember("autoApply", "true")
                        .build());
    }

    private String createClassName(TypeElement typeElement) {
        return "CodeBased" + typeElement.getSimpleName() + "Converter";
    }

    private String createPkgName(TypeElement typeElement, String configPkgName) {
        return StringUtils.isNotEmpty(configPkgName) ? configPkgName : typeElement.getEnclosingElement().toString();
    }

    @Override
    public Class<Annotation>[] applyAnnCls() {
        return new Class[]{GenCodeBasedEnumConverter.class};
    }

    @Override
    public <A extends Annotation> Class<A>[] ignoreAnnCls() {
        return new Class[0];
    }
}
