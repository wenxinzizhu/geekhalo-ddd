package com.geekhalo.ddd.lite.codegen.dto;

import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.EnableGenForEntity;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaAggregateVo;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaEntityVo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PUBLIC)
public final class GenDtoParser {
    private final TypeElement element;

    private String className;
    private String packageName;
    private String parentClassName;

    public GenDtoParser(TypeElement typeElement) {
        this.element = typeElement;
    }

    public void read(Annotation annotation){
        if (annotation instanceof GenDto){
            readFromGenVo((GenDto) annotation);
        }
        if (annotation instanceof EnableGenForAggregate){
            readFromEnableGenForAggregate((EnableGenForAggregate) annotation);
        }
        if (annotation instanceof EnableGenForEntity){
            readFromEnableGenForEntity((EnableGenForEntity) annotation);
        }

    }

    private void readFromEnableGenForEntity(EnableGenForEntity annotation) {
        this.className = "Base" + element.getSimpleName().toString() + "Dto";
        this.packageName = element.getEnclosingElement().toString();
        this.parentClassName = JpaEntityVo.class.getName();
    }

    private void readFromEnableGenForAggregate(EnableGenForAggregate annotation) {
        this.className = "Base" + element.getSimpleName().toString() + "Dto";
        this.packageName = element.getEnclosingElement().toString();
        this.parentClassName = JpaAggregateVo.class.getName();
    }

    private void readFromGenVo(GenDto genDto) {
        this.className = "Base" + element.getSimpleName().toString() + "Dto";

        this.packageName = genDto.pkgName();
        if (StringUtils.isEmpty(this.packageName)){
            this.packageName = element.getEnclosingElement().toString();
        }

        if (StringUtils.isNotEmpty(genDto.parent())){
            this.parentClassName = genDto.parent();
        }
    }

}
