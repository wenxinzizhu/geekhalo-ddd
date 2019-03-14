package com.geekhalo.ddd.lite.codegen.controller.request;

import javax.lang.model.element.ExecutableElement;

public interface RequestBodyInfoParser {
    RequestBodyInfo parseForMethod(ExecutableElement method);
}
