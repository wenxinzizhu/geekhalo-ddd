package com.geekhalo.ddd.lite.codegen.controller.request;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.Collection;
import java.util.List;
import java.util.Map;

abstract class RequestBodyInfoParserSupport {
    private final String pkg;
    private final String baseClsName;
    private final String resourceName;

    public RequestBodyInfoParserSupport(String pkg,
                                           String baseClsName) {
        this.pkg = pkg;
        this.baseClsName = baseClsName;
        this.resourceName = getResourceNameFromEndpointName(this.baseClsName);
    }

    public static String getResourceNameFromEndpointName(String endpointName) {
        if (endpointName.startsWith("Base")){
            endpointName = endpointName.substring("Base".length(), endpointName.length());
        }
        if (endpointName.endsWith("HttpEndpoint")){
            endpointName = endpointName.substring(0, endpointName.lastIndexOf("HttpEndpoint"));
        }
        if (endpointName.endsWith("Endpoint")){
            endpointName = endpointName.substring(0, endpointName.lastIndexOf("Endpoint"));
        }
        if (endpointName.endsWith("Controller")){
            endpointName = endpointName.substring(0, endpointName.lastIndexOf("Controller"));
        }
        return endpointName;
    }

    protected CreatedRequestBody createRequestBody(ExecutableElement method){
//        String pkg = getPackage(method);
        String requestBodyCls = getRequestBodyCls(method);
        return new CreatedRequestBody(pkg, baseClsName, requestBodyCls);
    }

    private String getRequestBodyCls(ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        return resourceName +
                methodName.substring(0, 1).toUpperCase() + methodName.substring(1, methodName.length())
                + "Req";
    }

//    private static String getPackage(ExecutableElement method) {
//        String fullClsName = method.getEnclosingElement().toString();
//        return getPackageFromFullClassName(fullClsName);
//    }

    protected static boolean notSimpleType(VariableElement variableElement) {
        String name = variableElement.asType().toString();
        name = name.substring(name.lastIndexOf(".") + 1);
        return !("string".equalsIgnoreCase(name) ||
                "int".equalsIgnoreCase(name) ||
                "integer".equalsIgnoreCase(name) ||
                "long".equalsIgnoreCase(name) ||
                "date".equalsIgnoreCase(name)||
                "instant".equalsIgnoreCase(name));
    }

    protected static boolean notCollectionType(VariableElement variableElement) {
        String name = variableElement.asType().toString();
        return (!name.startsWith(List.class.getName())) &&
                (! name.startsWith(Map.class.getName())) &&
                (! name.startsWith(Collection.class.getName()));
    }
}
