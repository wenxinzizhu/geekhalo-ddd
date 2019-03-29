package com.geekhalo.ddd.lite.codegen.application.model;

import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.support.MethodWriter;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import org.springframework.transaction.annotation.Transactional;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import static com.geekhalo.ddd.lite.codegen.application.Utils.getRepositoryGetterName;
import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.*;

public final class ModelBasedSupportMethodWriter implements MethodWriter {
    private final ModelBasedMethodMeta methodMeta;

    public ModelBasedSupportMethodWriter(ModelBasedMethodMeta methodMeta) {
        this.methodMeta = methodMeta;
    }

    @Override
    public void writeTo(JavaSource javaSource) {
        this.methodMeta.getCreateMethods().forEach(executableElement-> javaSource.addMethod(createCreateImpl(executableElement)));
        this.methodMeta.getUpdateMethods().forEach(executableElement -> javaSource.addMethod(createUpdateImpl(executableElement)));
    }

    private MethodSpec createCreateImpl(ExecutableElement executableElement){
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Transactional.class)
                .returns(this.methodMeta.getIdClassName());
        bindDescription(executableElement, methodBuilder);

        executableElement.getParameters().forEach(varElement->{
            methodBuilder.addParameter(createParameterSpecFromElement(varElement));
        });

        String repositoryGetterName = getRepositoryGetterName(this.methodMeta.getModelType());
        CodeBlock.Builder codeBuilder = CodeBlock.builder()
                .add("\t$T result = creatorFor(this.$L())\n" +
                                "            .publishBy(getDomainEventBus())\n" +
                                "            .instance(() -> $T.$L($L))\n" +
                                "            .call(); \n",
                        TypeName.get(methodMeta.getModelType().asType()),
                        repositoryGetterName,
                        TypeName.get(methodMeta.getModelType().asType()),
                        executableElement.getSimpleName().toString(),
                        createParamListStr(executableElement)
                );
        methodBuilder.addCode(codeBuilder.build());


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("logger().info(\"success to create {} using parm ")
                .append(createParamVarStr(executableElement))
                .append("\",")
                .append(createParamListStr(executableElement, "result.getId()"))
                .append(")");
        methodBuilder.addStatement(stringBuilder.toString());
        methodBuilder.addStatement("return result.getId()");

        return methodBuilder.build();
    }

    private MethodSpec createUpdateImpl(ExecutableElement executableElement) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(executableElement.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Transactional.class)
                .returns(TypeName.VOID)
                .addParameter(createIdParameter(this.methodMeta.getIdClassName()));

        bindDescription(executableElement, methodBuilder);

        executableElement.getParameters().forEach(varElement->{
            methodBuilder.addParameter(createParameterSpecFromElement(varElement));
        });

        String repositoryGetterName = getRepositoryGetterName(this.methodMeta.getModelType());
        CodeBlock.Builder codeBuilder = CodeBlock.builder()
                .add("\t$T result = updaterFor(this.$L())\n" +
                                "            .publishBy(getDomainEventBus())\n" +
                                "            .id(id)\n" +
                                "            .update(agg -> agg.$L($L))\n" +
                                "            .call(); \n",
                        TypeName.get(methodMeta.getModelType().asType()),
                        repositoryGetterName,
                        executableElement.getSimpleName().toString(),
                        createParamListStr(executableElement)
                );

        methodBuilder.addCode(codeBuilder.build());


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("logger().info(\"success to " + executableElement.getSimpleName().toString() +" for {} using parm ")
                .append(createParamVarStr(executableElement))
                .append("\", ")
                .append(createParamListStr(executableElement, "id"))
                .append(")");
        methodBuilder.addStatement(stringBuilder.toString());
        return methodBuilder.build();
    }

}
