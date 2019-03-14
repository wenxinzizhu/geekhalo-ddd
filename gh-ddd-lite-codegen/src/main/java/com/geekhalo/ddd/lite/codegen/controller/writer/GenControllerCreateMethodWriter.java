package com.geekhalo.ddd.lite.codegen.controller.writer;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.JavaSourceCollector;
import com.geekhalo.ddd.lite.codegen.TypeCollector;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerAnnotationParser;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerMethodMeta;
import com.geekhalo.ddd.lite.codegen.controller.request.CreateMethodRequestBodyParser;
import com.geekhalo.ddd.lite.codegen.controller.request.RequestBodyInfo;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.lang.model.element.Modifier;
import java.util.List;

import static com.geekhalo.ddd.lite.codegen.controller.writer.PathUtils.getPathFromMethod;
import static com.geekhalo.ddd.lite.codegen.utils.MethodUtils.createParamListStr;

public final class GenControllerCreateMethodWriter extends GenControllerMethodWriterSupport {


    public GenControllerCreateMethodWriter(GenControllerAnnotationParser parser,
                                           List<GenControllerMethodMeta.MethodMeta> methods, TypeCollector typeCollector) {
        super(parser, methods, typeCollector);
    }

    @Override
    protected void writeMethod(GenControllerMethodMeta.MethodMeta executableElement, TypeSpec.Builder builder, JavaSourceCollector javaSourceCollector) {
        builder.addMethod(createCreateMethod(executableElement, javaSourceCollector).build());
    }

    private MethodSpec.Builder createCreateMethod(GenControllerMethodMeta.MethodMeta executableElement, JavaSourceCollector javaSourceCollector) {
        String methodName = executableElement.getMethodName();
        Description description = executableElement.getDescription();
        String descriptionStr = description != null ? description.value() : "";
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ResponseBody.class)
                .addAnnotation(AnnotationSpec.builder(ApiOperation.class)
                        .addMember("value", "\""+ descriptionStr +"\"")
                        .addMember("nickname","\"" + methodName + "\"")
                        .build())
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "\"/_" + getPathFromMethod(methodName) +"\"")
                        .addMember("method", "$T.POST", ClassName.get(RequestMethod.class))
                        .build());

        RequestBodyInfo requestBodyInfo = new CreateMethodRequestBodyParser(getPkgName(), getBaseClassName())
                .parseForMethod(executableElement.getExecutableElement());
        requestBodyInfo.getSubType().forEach(javaSource -> javaSourceCollector.register(javaSource));

        builder.addParameter(ParameterSpec.builder(requestBodyInfo.getParameterType(), requestBodyInfo.getParameterName())
                .addAnnotation(RequestBody.class)
                .build());

        if (getParser().isWrapper()){
            if( "void".equalsIgnoreCase(executableElement.getReturnType().toString())){
                builder.addStatement("this.getApplication().$L($L)",
                        executableElement.getMethodName(),
                        createParamListStr(requestBodyInfo.getCallParams()));
                builder.addStatement("return $T.success()", ClassName.bestGuess(getParser().getWrapperCls()));

                builder.returns(ParameterizedTypeName.get(ClassName.bestGuess(getParser().getWrapperCls()), TypeName.VOID.box()));
            }else {
                builder.addStatement("return $T.success(this.getApplication().$L($L))",
                        ClassName.bestGuess(getParser().getWrapperCls()),
                        executableElement.getMethodName(), createParamListStr(requestBodyInfo.getCallParams()));

                builder.returns(ParameterizedTypeName.get(ClassName.bestGuess(getParser().getWrapperCls()),TypeName.get(executableElement.getReturnType())));
            }

        }else {
            builder.addStatement("return this.getApplication().$L($L)", executableElement.getMethodName(), createParamListStr(requestBodyInfo.getCallParams()));

            builder.returns(TypeName.get(executableElement.getReturnType()));
        }

        return builder;
    }


}
