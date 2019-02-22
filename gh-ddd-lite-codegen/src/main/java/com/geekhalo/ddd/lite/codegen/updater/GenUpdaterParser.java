package com.geekhalo.ddd.lite.codegen.updater;

import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.EnableGenForEntity;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

@Getter(AccessLevel.PUBLIC)
public class GenUpdaterParser {
    private final TypeElement typeElement;

    private String packageName;
    private String className;
    private String parentClassName;


    public GenUpdaterParser(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public void read(Annotation annotation){
        if (annotation instanceof GenUpdater){
            readFromGenUpdate((GenUpdater) annotation);
        }
        if (annotation instanceof EnableGenForAggregate){
            readFromEnableGenForAggregate((EnableGenForAggregate) annotation);
        }
        if (annotation instanceof EnableGenForEntity){
            readFromEnableGenForEntity((EnableGenForEntity) annotation);
        }

    }

    private void readFromEnableGenForEntity(EnableGenForEntity annotation) {
        this.packageName = typeElement.getEnclosingElement().toString();
        this.className = "Base" + typeElement.getSimpleName().toString() + "Updater";
    }

    private void readFromEnableGenForAggregate(EnableGenForAggregate annotation) {
        this.packageName = typeElement.getEnclosingElement().toString();
        this.className = "Base" + typeElement.getSimpleName().toString() + "Updater";

    }

    private void readFromGenUpdate(GenUpdater genUpdater) {
        this.packageName = typeElement.getEnclosingElement().toString();
        this.className = "Base" + typeElement.getSimpleName().toString() + "Updater";
        if (StringUtils.isNotEmpty(genUpdater.parent())){
            this.parentClassName = genUpdater.parent();
        }
    }
}
