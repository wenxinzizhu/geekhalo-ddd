package com.geekhalo.ddd.lite.codegen.application;

import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.domain.support.AbstractApplication;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.getParentPacketName;

@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PUBLIC)
public final class GenApplicationAnnotationParser {
    private static final String REPOSITORY = "Repository";
    private final TypeElement typeElement;

    private boolean enable = true;

    private boolean repository;
    private String modelName;
    private String pkgName;
    private String ifcName;
    private String impName;
    private String supperClassName;
    private String fullRepositoryName;

    private boolean genImpl;

    public GenApplicationAnnotationParser(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public boolean genImp(){
        return this.genImpl;
    }

    public void read(Annotation annotation){
        if (annotation instanceof GenApplication){
            readFromGenApplication((GenApplication) annotation);
        }
        if (annotation instanceof EnableGenForAggregate){
            readFromEnableGenForAggregate((EnableGenForAggregate) annotation);
        }
    }

    private void readFromEnableGenForAggregate(EnableGenForAggregate annotation) {
        String name = this.typeElement.getQualifiedName().toString();

        this.repository = false;
        this.modelName = name;
        this.fullRepositoryName = getDefaultRepositoryFromModel(name);

        this.pkgName = getDefaultPkgName();
        this.ifcName = getDefaultIfcName();
        this.impName = getDefaultImpName();
        this.supperClassName = getDefaultSupperClassName();
        this.genImpl = true;
    }

    private void readFromGenApplication(GenApplication annotation) {
        String name = this.typeElement.getQualifiedName().toString();
        this.repository = isRepository(name);
        if (this.repository){
            this.modelName = getModelNameFromRepository(name);
            this.fullRepositoryName = name;
        }else {
            this.modelName = name;
            this.fullRepositoryName = getDefaultRepositoryFromModel(name);
        }

        this.pkgName = initPkgName(annotation);
        this.ifcName = initIfcName(annotation);
        this.impName = initImpName(annotation);
        this.supperClassName = initSupperClassName(annotation);
        this.genImpl = annotation.genImpl();
    }


    private String initSupperClassName(GenApplication annotation) {
        String config = annotation.superClassName();
        if (StringUtils.isNotEmpty(config)){
            return config;
        }
        return getDefaultSupperClassName();
    }

    private String getDefaultSupperClassName(){
        return AbstractApplication.class.getName();
    }

    private String initImpName(GenApplication annotation) {
        String config = annotation.implementName();
        if (StringUtils.isNotEmpty(config)){
            return config;
        }
        return getDefaultImpName();
    }

    private String getDefaultImpName() {
        return this.ifcName + "Support";
    }

    private String initIfcName(GenApplication annotation) {
        String config = annotation.interfaceName();
        if (StringUtils.isNotEmpty(config)){
            return config;
        }
        return getDefaultIfcName();
    }

    private String getDefaultIfcName() {
        return "Base" + getSimpleName(modelName) + "Application";
    }

    private String initPkgName(GenApplication annotation) {
        String config = annotation.pkgName();
        if (StringUtils.isNotEmpty(config)){
            return config;
        }
        return getDefaultPkgName();
    }

    private String getDefaultPkgName() {
        String modelPkg = typeElement.getEnclosingElement().toString();
        if (modelPkg.contains(".domain")){
            return modelPkg.substring(0, modelPkg.indexOf(".domain")) + ".application";
        }
        return getParentPacketName(typeElement.getEnclosingElement().toString()) + ".application";
    }


    public boolean isRepository() {
        return this.repository;
    }

    public String getModelName() {
        return this.modelName;
    }

    public String getModelDtoName() {
        return getSimpleName(this.modelName) + "Dto";
    }

    public String getFullModelDtoName(){
        return this.modelName + "Dto";
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public String getIfcName() {
        return this.ifcName;
    }

    public String getFullIfcName() {
        return this.pkgName + "." + ifcName;

    }

    public String getImpName() {
        return this.impName;

    }

    public String getImpPkg(){
        return this.pkgName + ".impl";
    }

    public String getFullImplName() {
        return this.getImpPkg() + "." + impName;

    }

    public String getSuperClassName() {
        return this.supperClassName;
    }

    public String getFullRepository(){
        return this.fullRepositoryName;
    }

    private String getSimpleName(String name){
        return name.substring(name.lastIndexOf(".") + 1);
    }

    private String getDefaultRepositoryFromModel(String name) {
        return typeElement.getEnclosingElement().toString() + "." + getSimpleName(name) + REPOSITORY;
    }

    private String getModelNameFromRepository(String name) {
        return name.replace(REPOSITORY, "").replace(REPOSITORY.toLowerCase(), "domain");
    }

    private boolean isRepository(String name) {
        return name.toLowerCase().endsWith(REPOSITORY.toLowerCase());
    }
}
