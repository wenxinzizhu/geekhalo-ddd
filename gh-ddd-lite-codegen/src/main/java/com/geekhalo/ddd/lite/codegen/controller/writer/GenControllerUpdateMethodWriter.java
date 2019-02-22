package com.geekhalo.ddd.lite.codegen.controller.writer;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.TypeCollector;
import com.geekhalo.ddd.lite.codegen.controller.request.RequestBodyInfo;
import com.geekhalo.ddd.lite.codegen.controller.request.RequestBodyInfoUtils;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.List;

import static com.geekhalo.ddd.lite.codegen.controller.request.RequestBodyInfoUtils.parseAndCreateForUpdate;
import static com.geekhalo.ddd.lite.codegen.controller.writer.PathUtils.getPathFromMethod;
import static com.geekhalo.ddd.lite.codegen.utils.MethodUtils.createParamListStr;


public final class GenControllerUpdateMethodWriter extends GenControllerMethodWriterSupport {
    public GenControllerUpdateMethodWriter(String pkgName, RequestBodyInfoUtils.RequestBodyCreator creator, List<ExecutableElement> methods, TypeCollector typeCollector) {
        super(pkgName, creator, methods, typeCollector);
    }

    @Override
    protected void writeMethod(ExecutableElement executableElement, TypeSpec.Builder builder) {
        builder.addMethod(createUpdateMethod(executableElement).build());
    }

    private MethodSpec.Builder createUpdateMethod(ExecutableElement executableElement) {
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
                        .addMember("value", "\"{id}/_" + getPathFromMethod(methodName) +"\"")
                        .addMember("method", "$T.POST", ClassName.get(RequestMethod.class))
                        .build());

        ParameterSpec idParameter = ParameterSpec.builder(TypeName.LONG.box(), "id")
                .addAnnotation(AnnotationSpec.builder(PathVariable.class)
                        .addMember("value", "\"id\"")
                        .build())
                .build();
        builder.addParameter(idParameter);

        RequestBodyInfo requestBodyInfo = parseAndCreateForUpdate(executableElement, getPkgName(), getCreator());
        if (requestBodyInfo != null){
            builder.addParameter(ParameterSpec.builder(requestBodyInfo.getParameterType(), requestBodyInfo.getParameterName())
                    .addAnnotation(RequestBody.class)
                    .build());

            builder.addStatement("this.getApplication().$L($L)",
                    methodName,
                    createParamListStr(requestBodyInfo.getCallParams(), "id"));
        }else {
            builder.addStatement("this.getApplication().$L($L)",
                    methodName,
                    "id");
        }
        builder.returns(TypeName.VOID);
        return builder;
    }

}
