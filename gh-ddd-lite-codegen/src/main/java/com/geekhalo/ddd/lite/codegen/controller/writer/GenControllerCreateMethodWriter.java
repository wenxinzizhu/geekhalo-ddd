package com.geekhalo.ddd.lite.codegen.controller.writer;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.TypeCollector;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerAnnotationParser;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerMethodMeta;
import com.geekhalo.ddd.lite.codegen.controller.request.CreateMethodRequestBodyParser;
import com.geekhalo.ddd.lite.codegen.controller.request.RequestBodyInfo;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;

import static com.geekhalo.ddd.lite.codegen.controller.writer.PathUtils.getPathFromMethod;
import static com.geekhalo.ddd.lite.codegen.utils.MethodUtils.createParamListStr;

public final class GenControllerCreateMethodWriter extends GenControllerMethodWriterSupport {


    public GenControllerCreateMethodWriter(GenControllerAnnotationParser parser,
                                           List<GenControllerMethodMeta.MethodMeta> methods, TypeCollector typeCollector) {
        super(parser, methods, typeCollector);
    }

    @Override
    protected void writeMethod(GenControllerMethodMeta.MethodMeta executableElement, JavaSource typeBuilder) {
        String methodName = executableElement.getMethodName();
        Description description = executableElement.getDescription();
        String descriptionStr = description != null ? description.value() : "";
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ResponseBody.class)
                .addAnnotation(AnnotationSpec.builder(ApiOperation.class)
                        .addMember("value", "\""+ descriptionStr +"\"")
                        .addMember("nickname","\"" + methodName + "\"")
                        .build())
                .addAnnotation(AnnotationSpec.builder(PostMapping.class)
                        .addMember("value", "\"/_" + getPathFromMethod(methodName) +"\"")
                        .build());

        RequestBodyInfo requestBodyInfo = new CreateMethodRequestBodyParser(getPkgName(), getBaseClassName())
                .parseForMethod(executableElement.getExecutableElement());

        requestBodyInfo.getBodyType().forEach(innerBuilder -> typeBuilder.addType(innerBuilder.build()));

        methodBuilder.addParameter(ParameterSpec.builder(requestBodyInfo.getParameterType(), requestBodyInfo.getParameterName())
                .addAnnotation(RequestBody.class)
                .build());

        if (getParser().isWrapper()){
            if( "void".equalsIgnoreCase(executableElement.getReturnType().toString())){
                methodBuilder.addStatement("this.getApplication().$L($L)",
                        executableElement.getMethodName(),
                        createParamListStr(requestBodyInfo.getCallParams()));
                methodBuilder.addStatement("return $T.success()", ClassName.bestGuess(getParser().getWrapperCls()));

                methodBuilder.returns(ParameterizedTypeName.get(ClassName.bestGuess(getParser().getWrapperCls()), TypeName.VOID.box()));
            }else {
                methodBuilder.addStatement("return $T.success(this.getApplication().$L($L))",
                        ClassName.bestGuess(getParser().getWrapperCls()),
                        executableElement.getMethodName(), createParamListStr(requestBodyInfo.getCallParams()));

                methodBuilder.returns(ParameterizedTypeName.get(ClassName.bestGuess(getParser().getWrapperCls()),TypeName.get(executableElement.getReturnType())));
            }

        }else {
            methodBuilder.addStatement("return this.getApplication().$L($L)", executableElement.getMethodName(), createParamListStr(requestBodyInfo.getCallParams()));

            methodBuilder.returns(TypeName.get(executableElement.getReturnType()));
        }

        typeBuilder.addMethod(methodBuilder.build());
    }


}
