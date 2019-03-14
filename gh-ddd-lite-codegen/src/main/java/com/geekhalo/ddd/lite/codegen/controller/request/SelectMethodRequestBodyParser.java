package com.geekhalo.ddd.lite.codegen.controller.request;

import org.apache.commons.collections.CollectionUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;

public class SelectMethodRequestBodyParser
        extends RequestBodyInfoParserSupport
        implements RequestBodyInfoParser{

    public SelectMethodRequestBodyParser(String pkg, String baseClsName) {
        super(pkg, baseClsName);
    }

    @Override
    public RequestBodyInfo parseForMethod(ExecutableElement method) {
        List<? extends VariableElement> params = method.getParameters();
        if (CollectionUtils.isEmpty(params)){
            return null;
        }

        CreatedRequestBody createdRequestBody = createRequestBody(method);
        List<? extends VariableElement> parameters = method.getParameters();
        createdRequestBody.addParameters(parameters);
        return createdRequestBody;
    }
}
