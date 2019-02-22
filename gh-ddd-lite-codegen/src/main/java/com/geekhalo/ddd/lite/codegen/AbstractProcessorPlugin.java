package com.geekhalo.ddd.lite.codegen;


import com.squareup.javapoet.JavaFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

@Getter
@Setter(AccessLevel.PRIVATE)
public abstract class AbstractProcessorPlugin implements ProcessorPlugin{
    private final JavaSourceCollector javaSourceCollector = new JavaSourceCollector();
    private TypeCollector typeCollector;

    protected abstract void process(TypeElement typeElement, Annotation t);

    @Override
    public void init(TypeCollector typeCollector) {
        this.typeCollector = typeCollector;
    }

    @Override
    public void process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getRootElements()){
            if (!(element instanceof TypeElement)){
                continue;
            }

            for (Class<Annotation> ann : ignoreAnnCls()){
                if (element.getAnnotation(ann) != null){
                    break;
                }
            }

            for (Class<Annotation> ann : applyAnnCls()){
                Annotation annotation = element.getAnnotation(ann);
                if (annotation != null){
                    process((TypeElement) element, annotation);
                    break;
                }
            }
        }
    }

    @Override
    public final void write(Filer filer) {
        javaSourceCollector.getAllJavaSource()
                .forEach(javaSource -> createJavaFile(filer, javaSource));
    }

    private void createJavaFile(Filer filer, JavaSource javaSource) {
        try {
            JavaFile javaFile = JavaFile.builder(javaSource.getPkgName(), javaSource.getTypeSpecBuilder().build())
                    .addFileComment(" This codes are generated automatically. Do not modify!")
                    .build();
            javaFile.writeTo(filer);
//            System.out.println(javaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
