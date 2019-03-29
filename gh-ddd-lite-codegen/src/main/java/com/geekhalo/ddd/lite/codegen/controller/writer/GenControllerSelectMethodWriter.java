package com.geekhalo.ddd.lite.codegen.controller.writer;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.JavaSource;
import com.geekhalo.ddd.lite.codegen.TypeCollector;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerAnnotationParser;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerMethodMeta;
import com.geekhalo.ddd.lite.codegen.controller.request.RequestBodyInfo;
import com.geekhalo.ddd.lite.codegen.controller.request.SelectMethodRequestBodyParser;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static com.geekhalo.ddd.lite.codegen.controller.writer.PathUtils.getPathFromMethod;
import static com.geekhalo.ddd.lite.codegen.utils.MethodUtils.*;

public final class GenControllerSelectMethodWriter extends GenControllerMethodWriterSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenControllerSelectMethodWriter.class);

    public GenControllerSelectMethodWriter(GenControllerAnnotationParser parser,
                                           List<GenControllerMethodMeta.MethodMeta> methods,
                                           TypeCollector typeCollector) {
        super(parser, methods, typeCollector);
    }

    @Override
    protected void writeMethod(GenControllerMethodMeta.MethodMeta executableElement, JavaSource javaSource) {
        if (isGetByIdMethod(executableElement.getExecutableElement())){
            writeGetByIdMethod(executableElement, javaSource);
        }else if (isListMethod(executableElement.getExecutableElement())){
            writeListMethod(executableElement, javaSource);
        }else if (isPageMethod(executableElement.getExecutableElement())){
            writePageMethod(executableElement, javaSource);
        }else if (isOptionalMethod(executableElement.getExecutableElement())){
            writeOptionalMethod(executableElement, javaSource);
        }else {
            LOGGER.warn("failed to write method for {}", executableElement.getMethodName());
        }
    }

    private void writeOptionalMethod(GenControllerMethodMeta.MethodMeta executableElement, JavaSource javaSource) {
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
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "\"/_"+ getPathFromMethod(methodName) + "\"")
                        .addMember("method", "$T.POST", ClassName.get(RequestMethod.class))
                        .build());

        String returnType = executableElement.getReturnType().toString();
        RequestBodyInfo requestBodyInfo = new SelectMethodRequestBodyParser(getPkgName(), getBaseClassName())
                .parseForMethod(executableElement.getExecutableElement());
        requestBodyInfo.getBodyType().forEach(innerBuilder -> javaSource.addType(innerBuilder.build()));
        if (requestBodyInfo != null){
            methodBuilder.addParameter(ParameterSpec.builder(requestBodyInfo.getParameterType(), requestBodyInfo.getParameterName())
                    .addAnnotation(RequestBody.class)
                    .build());
            if (isOptionalMethod(executableElement.getExecutableElement())) {
                methodBuilder.addStatement("return this.getApplication().$L($L).orElse(null)",
                        methodName,
                        createParamListStr(requestBodyInfo.getCallParams()));
                String type = getTypeFromOptional(returnType);
                methodBuilder.returns(ClassName.bestGuess(type));
            }else {
                methodBuilder.addStatement("return this.getApplication().$L($L)",
                        methodName,
                        createParamListStr(requestBodyInfo.getCallParams()));
                methodBuilder.returns(TypeName.get(executableElement.getReturnType()));
            }
        }else {
            if (isOptionalMethod(executableElement.getExecutableElement())) {
                methodBuilder.addStatement("return this.getApplication().$L().orElse(null)",
                        methodName);
                String type = getTypeFromOptional(returnType);
                methodBuilder.returns(ClassName.bestGuess(type));
            }else {
                methodBuilder.addStatement("return this.getApplication().$L()",
                        methodName);
                methodBuilder.returns(TypeName.get(executableElement.getReturnType()));
            }
        }

        javaSource.addMethod(methodBuilder.build());
    }

    private void writeGetByIdMethod(GenControllerMethodMeta.MethodMeta executableElement, JavaSource javaSource) {
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
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "\"/{id}\"")
                        .addMember("method", "$T.GET", ClassName.get(RequestMethod.class))
                        .build());
        VariableElement idParams = getIdParam(executableElement.getExecutableElement());
        if (isLong(idParams)) {
            methodBuilder.addParameter(ParameterSpec.builder(ClassName.LONG.box(), "id")
                    .addAnnotation(AnnotationSpec.builder(PathVariable.class)
                            .addMember("value", "\"id\"")
                            .build())
                    .build());
        }else if(isBigInter(idParams)){
            methodBuilder.addParameter(ParameterSpec.builder(ClassName.get(BigInteger.class), "id")
                    .addAnnotation(AnnotationSpec.builder(PathVariable.class)
                            .addMember("value", "\"id\"")
                            .build())
                    .build());
        }else {
            methodBuilder.addParameter(ParameterSpec.builder(ClassName.get(String.class), "_id")
                    .addAnnotation(AnnotationSpec.builder(PathVariable.class)
                            .addMember("value", "\"id\"")
                            .build())
                    .build());
            methodBuilder.addStatement("$T id = $T.apply(_id)", idParams, idParams);
        }

        String returnType = executableElement.getExecutableElement().getReturnType().toString();
        if (getParser().isWrapper()){
            if (isOptional(returnType)){
                methodBuilder.addStatement("return $T.success(this.getApplication().getById(id).orElse(null))",
                        ClassName.bestGuess(getParser().getWrapperCls()));
                String type = getTypeFromOptional(returnType);
                methodBuilder.returns(ParameterizedTypeName.get(ClassName.bestGuess(getParser().getWrapperCls()), ClassName.bestGuess(type)) );
            }else {
                methodBuilder.addStatement("return $T.success(this.getApplication().getById(id))",
                        ClassName.bestGuess(getParser().getWrapperCls()));
                methodBuilder.returns(ParameterizedTypeName.get(ClassName.bestGuess(getParser().getWrapperCls()),
                        TypeName.get(executableElement.getExecutableElement().getReturnType())));
            }
        }else {
            if (isOptional(returnType)){
                methodBuilder.addStatement("return this.getApplication().getById(id).orElse(null)");
                String type = getTypeFromOptional(returnType);
                methodBuilder.returns(ClassName.bestGuess(type));
            }else {
                methodBuilder.addStatement("return this.getApplication().getById(id)");
                methodBuilder.returns(TypeName.get(executableElement.getExecutableElement().getReturnType()));
            }
        }

        javaSource.addMethod(methodBuilder.build());
    }


    private String getTypeFromOptional(String returnType) {
        return returnType.replace("java.util.Optional", "").replace("<", "").replace(">", "");
    }

    private String getTypeFromPage(String returnType) {
        return returnType.replace("org.springframework.data.domain.Page", "").replace("<", "").replace(">", "");
    }

    private boolean isOptional(String returnType) {
        return returnType.startsWith("java.util.Optional");
    }

    private MethodSpec.Builder writeListMethod(GenControllerMethodMeta.MethodMeta executableElement, JavaSource javaSource) {
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
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "\"/_"+ getPathFromMethod(methodName) + "\"")
                        .addMember("method", "$T.POST", ClassName.get(RequestMethod.class))
                        .build());

        if (getParser().isWrapper()){
            methodBuilder.returns(ParameterizedTypeName.get(ClassName.bestGuess(getParser().getWrapperCls()),TypeName.get(executableElement.getReturnType())));

            RequestBodyInfo requestBodyInfo = new SelectMethodRequestBodyParser(getPkgName(), getBaseClassName())
                    .parseForMethod(executableElement.getExecutableElement());
            requestBodyInfo.getBodyType().forEach(innerBuilder -> javaSource.addType(innerBuilder.build()));
            if (requestBodyInfo != null) {
                methodBuilder.addParameter(ParameterSpec.builder(requestBodyInfo.getParameterType(), requestBodyInfo.getParameterName())
                        .addAnnotation(RequestBody.class)
                        .build());
                methodBuilder.addStatement("return $T.success(this.getApplication().$L($L))",
                        ClassName.bestGuess(getParser().getWrapperCls()),
                        methodName,
                        createParamListStr(requestBodyInfo.getCallParams()));
            } else {
                methodBuilder.addStatement("return $T.success(this.getApplication().$L())",
                        ClassName.bestGuess(getParser().getWrapperCls()),
                        methodName);
            }
        }else {
            methodBuilder.returns(TypeName.get(executableElement.getReturnType()));

            RequestBodyInfo requestBodyInfo = new SelectMethodRequestBodyParser(getPkgName(), getBaseClassName())
                    .parseForMethod(executableElement.getExecutableElement());
            requestBodyInfo.getBodyType().forEach(innerBuilder -> javaSource.addType(innerBuilder.build()));
            if (requestBodyInfo != null) {
                methodBuilder.addParameter(ParameterSpec.builder(requestBodyInfo.getParameterType(), requestBodyInfo.getParameterName())
                        .addAnnotation(RequestBody.class)
                        .build());
                methodBuilder.addStatement("return this.getApplication().$L($L)",
                        methodName,
                        createParamListStr(requestBodyInfo.getCallParams()));
            } else {
                methodBuilder.addStatement("return this.getApplication().$L()",
                        methodName);
            }
        }

        return methodBuilder;
    }


    private void writePageMethod(GenControllerMethodMeta.MethodMeta executableElement, JavaSource javaSource) {
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
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "\"/_"+ getPathFromMethod(methodName) + "\"")
                        .addMember("method", "$T.POST", ClassName.get(RequestMethod.class))
                        .build());

        ClassName type = ClassName.bestGuess(this.getTypeFromPage(executableElement.getReturnType().toString()));
        ClassName pageVoClass = ClassName.bestGuess("com.geekhalo.ddd.lite.spring.mvc.PageVo");
        TypeName pageVoType = ParameterizedTypeName.get(pageVoClass, type);

        if (getParser().isWrapper()){
            methodBuilder.returns(ParameterizedTypeName.get(ClassName.bestGuess(getParser().getWrapperCls()), pageVoType));

            RequestBodyInfo requestBodyInfo = new SelectMethodRequestBodyParser(getPkgName(), getBaseClassName())
                    .parseForMethod(executableElement.getExecutableElement());
            requestBodyInfo.getBodyType().forEach(innerBuilder -> javaSource.addType(innerBuilder.build()));
            if (requestBodyInfo != null) {
                methodBuilder.addParameter(ParameterSpec.builder(requestBodyInfo.getParameterType(), requestBodyInfo.getParameterName())
                        .addAnnotation(RequestBody.class)
                        .build());
                methodBuilder.addStatement("return $T.success($T.apply(this.getApplication().$L($L)))",
                        ClassName.bestGuess(getParser().getWrapperCls()),
                        pageVoClass,
                        methodName,
                        createParamListStr(requestBodyInfo.getCallParams()));
            } else {
                methodBuilder.addStatement("return $T.success($T.apply(this.getApplication().$L()))",
                        ClassName.bestGuess(getParser().getWrapperCls()),
                        pageVoClass,
                        methodName);
            }
        }else {
            methodBuilder.returns(pageVoType);

            RequestBodyInfo requestBodyInfo = new SelectMethodRequestBodyParser(getPkgName(), getBaseClassName())
                    .parseForMethod(executableElement.getExecutableElement());
            requestBodyInfo.getBodyType().forEach(innerBuilder -> javaSource.addType(innerBuilder.build()));
            if (requestBodyInfo != null) {
                methodBuilder.addParameter(ParameterSpec.builder(requestBodyInfo.getParameterType(), requestBodyInfo.getParameterName())
                        .addAnnotation(RequestBody.class)
                        .build());
                methodBuilder.addStatement("return $T.apply(this.getApplication().$L($L))",
                        pageVoType,
                        methodName,
                        createParamListStr(requestBodyInfo.getCallParams()));
            } else {
                methodBuilder.addStatement("return $T.apply(this.getApplication().$L())",
                        pageVoType,
                        methodName);
            }
        }
        javaSource.addMethod(methodBuilder.build());
    }

}
