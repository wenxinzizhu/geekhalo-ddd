package com.geekhalo.ddd.lite.codegen;


import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;

public interface ProcessorPlugin{
    <A extends Annotation> Class<A>[] applyAnnCls();
    <A extends Annotation> Class<A>[] ignoreAnnCls();
    void init(TypeCollector typeCollector);
    void process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);
    void write(Filer filer);
}
