package com.geekhalo.ddd.lite.codegen.dto;

import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.EnableGenForEntity;
import com.geekhalo.ddd.lite.codegen.utils.TypeUtils;
import com.geekhalo.ddd.lite.domain.support.AbstractAggregateDto;
import com.geekhalo.ddd.lite.domain.support.AbstractEntityDto;
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
    private String idType;

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
        this.parentClassName = AbstractEntityDto.class.getName();
        this.idType = TypeUtils.getIdClassName(element);
    }

    private void readFromEnableGenForAggregate(EnableGenForAggregate annotation) {
        this.className = "Base" + element.getSimpleName().toString() + "Dto";
        this.packageName = element.getEnclosingElement().toString();
        this.parentClassName = AbstractAggregateDto.class.getName();
        this.idType = TypeUtils.getIdClassName(element);
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
        this.idType = TypeUtils.getIdClassName(this.element);
    }

}
