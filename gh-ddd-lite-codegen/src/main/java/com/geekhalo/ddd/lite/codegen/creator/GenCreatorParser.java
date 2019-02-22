package com.geekhalo.ddd.lite.codegen.creator;

import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.EnableGenForEntity;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

@Getter(AccessLevel.PUBLIC)
public class GenCreatorParser {
    private final TypeElement typeElement;

    private GenCreatorType type;
    private String packageName;
    private String className;
    private String parentClassName;


    public GenCreatorParser(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public void read(Annotation annotation){
        if (annotation instanceof GenCreator){
            readFromGenCreator((GenCreator) annotation);
        }
        if (annotation instanceof EnableGenForAggregate){
            readFromEnableGenForAggregate((EnableGenForAggregate) annotation);
        }
        if (annotation instanceof EnableGenForEntity){
            readFromEnableGenForEntity((EnableGenForEntity)annotation);
        }
    }

    private void readFromEnableGenForEntity(EnableGenForEntity annotation) {
        this.type = GenCreatorType.JAVA_BEAN;
        this.packageName = typeElement.getEnclosingElement().toString();
        this.className = "Base" + typeElement.getSimpleName().toString() + "Creator";
        this.parentClassName = "";
    }

    private void readFromEnableGenForAggregate(EnableGenForAggregate annotation) {
        this.type = GenCreatorType.JAVA_BEAN;
        this.packageName = typeElement.getEnclosingElement().toString();
        this.className = "Base" + typeElement.getSimpleName().toString() + "Creator";
        this.parentClassName = "";
    }

    private void readFromGenCreator(GenCreator genCreator) {
        this.type = genCreator.type();
        this.packageName = typeElement.getEnclosingElement().toString();
        this.className = "Base" + typeElement.getSimpleName().toString() + "Creator";
        if (StringUtils.isNotEmpty(genCreator.parent())){
            this.parentClassName = genCreator.parent();
        }
    }
}
