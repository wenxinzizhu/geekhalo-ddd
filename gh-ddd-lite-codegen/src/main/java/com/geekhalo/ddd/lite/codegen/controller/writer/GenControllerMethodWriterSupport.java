package com.geekhalo.ddd.lite.codegen.controller.writer;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.TypeCollector;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerAnnotationParser;
import com.geekhalo.ddd.lite.codegen.controller.GenControllerMethodMeta;
import com.geekhalo.ddd.lite.codegen.support.MethodWriter;
import com.squareup.javapoet.TypeSpec;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.math.BigInteger;
import java.util.List;

@Getter(AccessLevel.PROTECTED)
abstract class GenControllerMethodWriterSupport implements MethodWriter {
    private final GenControllerAnnotationParser parser;
    private final String pkgName;
    private final List<GenControllerMethodMeta.MethodMeta> methods;
    private final TypeCollector typeCollector;

    protected GenControllerMethodWriterSupport(GenControllerAnnotationParser parser,
                                               List<GenControllerMethodMeta.MethodMeta> methods,
                                               TypeCollector typeCollector) {
        this.parser = parser;
        this.pkgName = parser.getPkgName();
        this.methods = methods;
        this.typeCollector = typeCollector;
    }

    @Override
    public void writeTo(TypeSpec.Builder builder) {
        if (!CollectionUtils.isEmpty(methods)){
            this.methods.stream()
                    .filter(methodMeta -> !methodMeta.isIngnore())
                    .forEach(executableElement -> writeMethod(executableElement, builder));
        }
    }

    protected String getBaseClassName(){
        return getParser().getSimpleEndpointName();
    }

    protected Description getDescription(ExecutableElement executableElement){
        Description description = executableElement.getAnnotation(Description.class);
        if (description != null){
            return description;
        }
        TypeElement typeElement = getTypeElement(executableElement);
        if (typeElement == null){
            return null;
        }
        description = getDescriptionFormSuper(typeElement, executableElement);
        if (description != null){
            return description;
        }
        return getDescriptionFormInterface(typeElement, executableElement);
    }

    private Description getDescriptionFormInterface(TypeElement typeElement, ExecutableElement executableElement) {
        for (TypeMirror typeMirror : typeElement.getInterfaces()){
            Element superElement = this.typeCollector.getByName(typeMirror.toString());
            if (superElement == null){
                continue;
            }
            Description description = getDescriptionFromTypeElement(executableElement, typeElement);
            if (description == null){
                continue;
            }
            return description;
        }
        return null;
    }



    private Description getDescriptionFormSuper(TypeElement typeElement, ExecutableElement executableElement){

        TypeMirror superType = typeElement.getSuperclass();
        if (superType == null){
            return null;
        }
        Element superElement = this.typeCollector.getByName(superType.toString());
        if (superElement == null){
            return null;
        }

        return getDescriptionFromTypeElement(executableElement, superElement);
    }

    private Description getDescriptionFromTypeElement(ExecutableElement executableElement, Element superElement) {
        ExecutableElement method = ElementFilter.methodsIn(superElement.getEnclosedElements()).stream()
                .filter(m->isEquals(m, executableElement))
                .findAny()
                .orElse(null);
        if (method == null){
            return null;
        }
        Description description = method.getAnnotation(Description.class);
        if (description != null){
            return description;
        }
        return null;
    }

    private boolean isEquals(ExecutableElement element1, ExecutableElement element2){
        return element1.getSimpleName().equals(element2.getSimpleName()) && isEquals(element1.getParameters(), element2.getParameters());
    }

    private boolean isEquals(List<? extends VariableElement> parameters, List<? extends VariableElement> parameters1) {
        if (parameters.size() != parameters1.size()){
            return false;
        }
        for (int i=0;i<parameters.size();i++){
            VariableElement var1 = parameters.get(i);
            VariableElement var2 = parameters1.get(i);
            if (!var1.asType().toString().equals(var2.asType().toString())){
                return false;
            }
        }
        return true;
    }

    private TypeElement getTypeElement(ExecutableElement executableElement){
        Element type = executableElement.getEnclosingElement();
        if (type == null){
            return null;
        }
        if (!(type instanceof TypeElement)){
            return null;
        }
        return (TypeElement) type;
    }

    protected abstract void writeMethod(GenControllerMethodMeta.MethodMeta executableElement, TypeSpec.Builder builder);

    protected boolean isBigInter(VariableElement idParams) {
        return idParams.asType().toString().equals(BigInteger.class.getName());
    }

    protected boolean isLong(VariableElement idParams) {
        return idParams.asType().toString().equals(Long.class.getName());
    }

    protected VariableElement getIdParam(ExecutableElement executableElement) {
        return executableElement.getParameters().get(0);
    }

}
