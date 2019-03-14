package com.geekhalo.ddd.lite.codegen.controller.writer;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.JavaSourceCollector;
import com.geekhalo.ddd.lite.codegen.TypeCollector;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerAnnotationParser;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerMethodMeta;
import com.geekhalo.ddd.lite.codegen.controller.request.RequestBodyInfo;
import com.geekhalo.ddd.lite.codegen.controller.request.UpdateMethodRequestBodyParser;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.math.BigInteger;
import java.util.List;

import static com.geekhalo.ddd.lite.codegen.controller.writer.PathUtils.getPathFromMethod;
import static com.geekhalo.ddd.lite.codegen.utils.MethodUtils.createParamListStr;


public final class GenControllerUpdateMethodWriter extends GenControllerMethodWriterSupport {
    public GenControllerUpdateMethodWriter(GenControllerAnnotationParser parser,
                                           List<GenControllerMethodMeta.MethodMeta> methods,
                                           TypeCollector typeCollector) {
        super(parser, methods, typeCollector);
    }

    @Override
    protected void writeMethod(GenControllerMethodMeta.MethodMeta executableElement, TypeSpec.Builder builder, JavaSourceCollector javaSourceCollector) {
        MethodSpec.Builder method = createUpdateMethod(executableElement, javaSourceCollector);
        builder.addMethod(method.build());
    }

    private MethodSpec.Builder createUpdateMethod(GenControllerMethodMeta.MethodMeta executableElement,
                                                  JavaSourceCollector javaSourceCollector) {
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
                        .addMember("value", "\"{id}/_" + getPathFromMethod(methodName) +"\"")
                        .addMember("method", "$T.POST", ClassName.get(RequestMethod.class))
                        .build());
        VariableElement idParam = getIdParam(executableElement.getExecutableElement());
        if (isLong(idParam)) {
            ParameterSpec idParameter = ParameterSpec.builder(TypeName.LONG.box(), "id")
                    .addAnnotation(AnnotationSpec.builder(PathVariable.class)
                            .addMember("value", "\"id\"")
                            .build())
                    .build();
            builder.addParameter(idParameter);
        }else if (isBigInter(idParam)){
            ParameterSpec idParameter = ParameterSpec.builder(ClassName.get(BigInteger.class), "id")
                    .addAnnotation(AnnotationSpec.builder(PathVariable.class)
                            .addMember("value", "\"id\"")
                            .build())
                    .build();
            builder.addParameter(idParameter);
        }else {
            ParameterSpec idParameter = ParameterSpec.builder(ClassName.get(String.class), "_id")
                    .addAnnotation(AnnotationSpec.builder(PathVariable.class)
                            .addMember("value", "\"id\"")
                            .build())
                    .build();
            builder.addParameter(idParameter);

            builder.addStatement("$T id = $T.apply(_id)", idParam, idParam);
        }

        RequestBodyInfo requestBodyInfo = new UpdateMethodRequestBodyParser(getPkgName(), getBaseClassName())
                .parseForMethod(executableElement.getExecutableElement());
        if (requestBodyInfo != null){
            requestBodyInfo.getSubType().forEach(javaSource -> javaSourceCollector.register(javaSource));
        }
        if (getParser().isWrapper()){
            if (requestBodyInfo != null) {
                builder.addParameter(ParameterSpec.builder(requestBodyInfo.getParameterType(), requestBodyInfo.getParameterName())
                        .addAnnotation(RequestBody.class)
                        .build());

                builder.addStatement("this.getApplication().$L($L)",
                        methodName,
                        createParamListStr(requestBodyInfo.getCallParams(), "id"));
            } else {
                builder.addStatement("this.getApplication().$L($L)",
                        methodName,
                        "id");
            }
            builder.addStatement("return $T.success(null)", ClassName.bestGuess(getParser().getWrapperCls()));
            builder.returns(ParameterizedTypeName.get(ClassName.bestGuess(getParser().getWrapperCls()), TypeName.VOID.box()));
        }else {
            if (requestBodyInfo != null) {
                builder.addParameter(ParameterSpec.builder(requestBodyInfo.getParameterType(), requestBodyInfo.getParameterName())
                        .addAnnotation(RequestBody.class)
                        .build());

                builder.addStatement("this.getApplication().$L($L)",
                        methodName,
                        createParamListStr(requestBodyInfo.getCallParams(), "id"));
            } else {
                builder.addStatement("this.getApplication().$L($L)",
                        methodName,
                        "id");
            }
            builder.returns(TypeName.VOID);
        }


        return builder;
    }

}
