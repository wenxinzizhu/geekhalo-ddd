package com.geekhalo.ddd.lite.codegen.controller.writer;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.TypeCollector;
import com.geekhalo.ddd.lite.codegen.controller.request.RequestBodyInfo;
import com.geekhalo.ddd.lite.codegen.controller.request.RequestBodyInfoUtils;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.List;

import static com.geekhalo.ddd.lite.codegen.controller.request.RequestBodyInfoUtils.parseAndCreateOfAllParams;
import static com.geekhalo.ddd.lite.codegen.controller.writer.PathUtils.getPathFromMethod;
import static com.geekhalo.ddd.lite.codegen.utils.MethodUtils.createParamListStr;

public final class GenControllerCreateMethodWriter extends GenControllerMethodWriterSupport {
    public GenControllerCreateMethodWriter(String pkgName, RequestBodyInfoUtils.RequestBodyCreator creator, List<ExecutableElement> methods, TypeCollector typeCollector) {
        super(pkgName, creator, methods, typeCollector);
    }

    @Override
    protected void writeMethod(ExecutableElement executableElement, TypeSpec.Builder builder) {
        builder.addMethod(createCreateMethod(executableElement).build());
    }

    private MethodSpec.Builder createCreateMethod(ExecutableElement executableElement) {
        String methodName = executableElement.getSimpleName().toString();
        Description description = getDescription(executableElement);
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

        RequestBodyInfo requestBodyInfo =  parseAndCreateOfAllParams(executableElement, getPkgName(), getCreator());
        builder.addParameter(ParameterSpec.builder(requestBodyInfo.getParameterType(), requestBodyInfo.getParameterName())
                .addAnnotation(RequestBody.class)
                .build());
        builder.addStatement("return this.getApplication().$L($L)", executableElement.getSimpleName().toString(), createParamListStr(requestBodyInfo.getCallParams()));

        builder.returns(TypeName.get(executableElement.getReturnType()));
        return builder;
    }


}
