package com.geekhalo.ddd.lite.codegen.application.repository;

import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.support.MethodWriter;
import com.squareup.javapoet.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.geekhalo.ddd.lite.codegen.application.Utils.getRepositoryGetterName;
import static com.geekhalo.ddd.lite.codegen.application.repository.Utils.*;
import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.*;

public final class RepositoryBasedSupportMethodWriter implements MethodWriter {
    private final RepositoryBasedMethodMeta methodMeta;
    private final String convert;
    private final String convertList;
    private final String convertPage;
    public RepositoryBasedSupportMethodWriter(RepositoryBasedMethodMeta methodMeta) {
        this.methodMeta = methodMeta;
        this.convert = getConvertMethodName(this.methodMeta.getModelType());
        this.convertList = getConvertListMethodName(this.methodMeta.getModelType());
        this.convertPage= getConvertPageMethodName(this.methodMeta.getModelType());
    }

    @Override
    public void writeTo(JavaSource javaSource) {
        bindConverterMethod(javaSource);
        methodMeta.getQueryMethods().forEach(executableElement -> {
            {
                MethodSpec method = createSupportMethod1(executableElement);
                if (method != null) {
                    javaSource.addMethod(method);
                }
            }
            {
                MethodSpec method = createSupportMethod(executableElement);
                if (method != null) {
                    javaSource.addMethod(method);
                }
            }
        });
    }

    private MethodSpec createSupportMethod1(ExecutableElement executableElement){
        try {
            TypeMirror returnType = executableElement.getReturnType();
            TypeVariableName elementTypeName = TypeVariableName.get("T");
            TypeName returnTypeName = getReturnTypeName(returnType, methodMeta, elementTypeName);
            if (returnTypeName == null){
                return null;
            }
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                    .addModifiers(Modifier.PUBLIC)
                    .addTypeVariable(TypeVariableName.get("T"))
                    .addAnnotation(AnnotationSpec.builder(Transactional.class)
                            .addMember("readOnly", "true")
                            .build())
                    .returns(returnTypeName);
            bindDescription(executableElement, methodBuilder);

            executableElement.getParameters().forEach(varElement->{
                methodBuilder.addParameter(createParameterSpecFromElement(varElement));
            });


            methodBuilder.addParameter(ParameterSpec.builder(
                    ParameterizedTypeName.get(ClassName.get(Function.class), ClassName.get(methodMeta.getModelType()), elementTypeName),
                    "converter")
                    .build());

            String repositoryGetterName = getRepositoryGetterName(this.methodMeta.getModelType());
            CodeBlock.Builder codeBuilder = CodeBlock.builder()
                    .add("$T result = this.$L().$L($L);\n",
                            TypeName.get(returnType),
                            repositoryGetterName,
                            executableElement.getSimpleName().toString(),
                            createParamListStr(executableElement)
                    );

            if (isOptional(returnType)) {
                codeBuilder.add("return result.map(converter);\n");
            }else if (isList(returnType)){
                codeBuilder.add("return $L(result, converter);\n", this.convertList);
            }else if (isPage(returnType)){
                codeBuilder.add("return $L(result, converter);\n", this.convertPage);
            }else if (isModel(returnType, methodMeta)){
                codeBuilder.add("return converter.apply(result);\n");
            }else {
                codeBuilder.add("return result;\n");
            }

            methodBuilder.addCode(codeBuilder.build());

            return methodBuilder.build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private MethodSpec createSupportMethod(ExecutableElement executableElement){
        TypeMirror returnType = executableElement.getReturnType();
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(Transactional.class)
                        .addMember("readOnly", "true")
                        .build())
                .returns(getReturnTypeName(returnType, methodMeta));
        bindDescription(executableElement, methodBuilder);

        executableElement.getParameters().forEach(varElement->{
            methodBuilder.addParameter(createParameterSpecFromElement(varElement));
        });

        String repositoryGetterName = getRepositoryGetterName(this.methodMeta.getModelType());
        CodeBlock.Builder codeBuilder = CodeBlock.builder()
                .add("$T result = this.$L().$L($L);\n",
                        TypeName.get(returnType),
                        repositoryGetterName,
                        executableElement.getSimpleName().toString(),
                        createParamListStr(executableElement)
                );

        if (isOptional(returnType)) {
            codeBuilder.add("return result.map(this::$L);\n", this.convert);
        }else if (isList(returnType)){
            codeBuilder.add("return $L(result);\n", this.convertList);
        }else if (isPage(returnType)){
            codeBuilder.add("return $L(result);\n", this.convertPage);
        }else if (isModel(returnType, methodMeta)){
            codeBuilder.add("return $L(result);\n", this.convert);
        }else {
            codeBuilder.add("return result;\n");
        }

        methodBuilder.addCode(codeBuilder.build());


//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("logger().info(\"success to create {} using parm ")
//                .append(createParamVarStr(executableElement))
//                .append("\",")
//                .append(createParamListStr(executableElement, "result.getId()"))
//                .append(")");
//        methodBuilder.addStatement(stringBuilder.toString());
//        methodBuilder.addStatement("return result");

        return methodBuilder.build();
    }

    private void bindConverterMethod(JavaSource javaSource) {

        MethodSpec covertMethodList = MethodSpec.methodBuilder(convertList)
                .addModifiers(Modifier.PROTECTED)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(ParameterizedTypeName.get(ClassName.get(List.class),
                        TypeVariableName.get("T")))
                .addParameter(ParameterizedTypeName.get(ClassName.get(List.class),
                        ClassName.get(methodMeta.getModelType())), "src")
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class),ClassName.get(methodMeta.getModelType()), TypeVariableName.get("T")), "converter")
                .addStatement("if ($T.isEmpty(src)) return $T.emptyList()", ClassName.get(CollectionUtils.class), ClassName.get(Collections.class))
                .addStatement("return src.stream().map(converter).collect($T.toList())", ClassName.get(Collectors.class))
                .build();
        javaSource.addMethod(covertMethodList);

        MethodSpec convertMethodPage = MethodSpec.methodBuilder(convertPage)
                .addModifiers(Modifier.PROTECTED)
                .addTypeVariable(TypeVariableName.get("T"))
                .returns(ParameterizedTypeName.get(ClassName.get(Page.class),
                        TypeVariableName.get("T")))
                .addParameter(ParameterizedTypeName.get(ClassName.get(Page.class),
                        ClassName.get(methodMeta.getModelType())), "src")
                .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class),ClassName.get(methodMeta.getModelType()), TypeVariableName.get("T")), "converter")
                .addStatement("return src.map(converter)")
                .build();
        javaSource.addMethod(convertMethodPage);



        MethodSpec convertMethod = MethodSpec.methodBuilder(convert)
                .addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED)
                .returns(ClassName.get(methodMeta.getModelVoType()))
                .addParameter(ClassName.get(methodMeta.getModelType()), "src")
                .build();
        javaSource.addMethod(convertMethod);



        MethodSpec listConvertMethod = MethodSpec.methodBuilder(convertList)
                .addModifiers(Modifier.PROTECTED)
                .returns(ParameterizedTypeName.get(ClassName.get(List.class),
                        ClassName.get(methodMeta.getModelVoType())))
                .addParameter(ParameterizedTypeName.get(ClassName.get(List.class),
                        ClassName.get(methodMeta.getModelType())), "src")
                .addStatement("return $L(src, this::$L)", convertList, convert)
                .build();
        javaSource.addMethod(listConvertMethod);

        MethodSpec pageConvertMethod = MethodSpec.methodBuilder(convertPage)
                .addModifiers(Modifier.PROTECTED)
                .returns(ParameterizedTypeName.get(ClassName.get(Page.class),
                        ClassName.get(methodMeta.getModelVoType())))
                .addParameter(ParameterizedTypeName.get(ClassName.get(Page.class),
                        ClassName.get(methodMeta.getModelType())), "src")
                .addStatement("return $L(src, this::$L)", convertPage, convert)
                .build();
        javaSource.addMethod(pageConvertMethod);
    }
}
