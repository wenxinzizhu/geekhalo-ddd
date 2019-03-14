package com.geekhalo.ddd.lite.codegen.controller.request;

import org.apache.commons.collections.CollectionUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;

public class CreateMethodRequestBodyParser
        extends RequestBodyInfoParserSupport
        implements RequestBodyInfoParser{


    public CreateMethodRequestBodyParser(String pkg, String baseClsName) {
        super(pkg, baseClsName);
    }

    @Override
    public RequestBodyInfo parseForMethod(ExecutableElement method) {
        List<? extends VariableElement> params = method.getParameters();
        if (CollectionUtils.isEmpty(params)){
            new IllegalArgumentException("Create Method must have params");
        }

        if (params.size() == 1 && notSimpleType(params.get(0)) && notCollectionType(params.get(0))){
            return new SingleRequestBody(params.get(0));
        }

        CreatedRequestBody createdRequestBody = createRequestBody(method);
        createdRequestBody.addParameters(method.getParameters());
        return createdRequestBody;
    }



}
