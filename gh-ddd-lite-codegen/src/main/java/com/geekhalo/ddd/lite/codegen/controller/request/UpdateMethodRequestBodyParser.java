package com.geekhalo.ddd.lite.codegen.controller.request;

import org.apache.commons.collections.CollectionUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;

public final class UpdateMethodRequestBodyParser
        extends RequestBodyInfoParserSupport
        implements RequestBodyInfoParser{


    public UpdateMethodRequestBodyParser(String pkg, String baseClsName) {
        super(pkg, baseClsName);
    }

    @Override
    public RequestBodyInfo parseForMethod(ExecutableElement method) {
        List<? extends VariableElement> params = method.getParameters();
        if (CollectionUtils.isEmpty(params)){
            throw new IllegalArgumentException("update Method must have params");
        }

        if (params.size() == 1){
            return null;
        }

        if (params.size() == 2 && notSimpleType(params.get(1))){
            return new SingleRequestBody(params.get(1));
        }

        CreatedRequestBody createdRequestBody = createRequestBody(method);
        List<? extends VariableElement> parameters = method.getParameters();
        createdRequestBody.addParameters(parameters.subList(1, parameters.size()));
        return createdRequestBody;
    }
}
