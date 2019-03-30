package com.geekhalo.ddd.lite.codegen.application;

import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.domain.support.AbstractApplication;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Optional;

import static com.geekhalo.ddd.lite.codegen.utils.TypeUtils.getParentPacketName;

@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PUBLIC)
public final class GenApplicationAnnotationParser {
    private static final String REPOSITORY = "Repository";
    private final TypeElement typeElement;

    private boolean repository;
    private String modelName;


    private String impPkgName;
    private String impName;

    private String ifcPkgName;
    private String ifcName;


    private String supperClassName;
    private String fullRepositoryName;

    private boolean genImpl;
    private boolean genIfc;

    public GenApplicationAnnotationParser(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public boolean genIfc(){
        return this.genIfc;
    }

    public boolean genImp(){
        return this.genImpl;
    }

    public void read(Annotation annotation){
        if (annotation instanceof GenApplication){
            readFromGenApplication((GenApplication) annotation);
        }
        if (annotation instanceof GenMixedApplication){
            readFromGenMixedApplication((GenMixedApplication) annotation);
        }
        if (annotation instanceof GenSingleApplication){
            readFromGenSingleApplication((GenSingleApplication) annotation);
        }
        if (annotation instanceof EnableGenForAggregate){
            readFromEnableGenForAggregate((EnableGenForAggregate) annotation);
        }
    }

    private void readFromGenMixedApplication(GenMixedApplication annotation) {
        String name = this.typeElement.getQualifiedName().toString();
        this.repository = isRepository(name);
        if (this.repository){
            this.modelName = getModelNameFromRepository(name);
            this.fullRepositoryName = name;
        }else {
            this.modelName = name;
            this.fullRepositoryName = getDefaultRepositoryFromModel(name);
        }

        this.supperClassName = initSupperClassName(annotation.superClassName());

        this.genIfc = annotation.genIfc();
        this.ifcPkgName = Optional.ofNullable(annotation.ifcPkgName())
                .filter(StringUtils::isNotEmpty)
                .orElse(getDefaultPkgName());
        this.ifcName = initIfcName(annotation.interfaceName());

        this.genImpl = annotation.genImpl();
        this.impPkgName = Optional.ofNullable(annotation.implPkgName())
                .filter(StringUtils::isNotEmpty)
                .orElse(this.ifcPkgName);
        this.impName = initImpName(annotation.implementName());
    }

    private void readFromGenSingleApplication(GenSingleApplication annotation) {
        String name = this.typeElement.getQualifiedName().toString();
        this.repository = isRepository(name);
        if (this.repository){
            this.modelName = getModelNameFromRepository(name);
            this.fullRepositoryName = name;
        }else {
            this.modelName = name;
            this.fullRepositoryName = getDefaultRepositoryFromModel(name);
        }

        this.supperClassName = initSupperClassName(annotation.superClassName());

        this.genIfc = annotation.genIfc();
        this.ifcPkgName = initPkgName(annotation.pkgName());
        this.ifcName = initIfcName(annotation.interfaceName());

        this.genImpl = annotation.genImpl();
        this.impPkgName = ifcPkgName + ".impl";
        this.impName = initImpName(annotation.implementName());

    }

    private void readFromEnableGenForAggregate(EnableGenForAggregate annotation) {
        String name = this.typeElement.getQualifiedName().toString();

        this.repository = false;
        this.modelName = name;
        this.fullRepositoryName = getDefaultRepositoryFromModel(name);

        this.genIfc = true;
        this.ifcPkgName = getDefaultPkgName();
        this.ifcName = getDefaultIfcName();

        this.genImpl = true;
        this.impPkgName = this.ifcPkgName + ".impl";
        this.impName = getDefaultImpName();
        this.supperClassName = getDefaultSupperClassName();
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


        this.supperClassName = initSupperClassName(annotation.superClassName());

        this.genIfc = annotation.genIfc();
        this.ifcPkgName = initPkgName(annotation.pkgName());
        this.ifcName = initIfcName(annotation.interfaceName());

        this.genImpl = annotation.genImpl();
        this.impPkgName = ifcPkgName + ".impl";
        this.impName = initImpName(annotation.implementName());
    }


    private String initSupperClassName(String config) {
        if (StringUtils.isNotEmpty(config)){
            return config;
        }
        return getDefaultSupperClassName();
    }

    private String getDefaultSupperClassName(){
        return AbstractApplication.class.getName();
    }

    private String initImpName(String config) {
        if (StringUtils.isNotEmpty(config)){
            return config;
        }
        return getDefaultImpName();
    }

    private String getDefaultImpName() {
        return this.ifcName + "Support";
    }

    private String initIfcName(String config) {
        if (StringUtils.isNotEmpty(config)){
            return config;
        }
        return getDefaultIfcName();
    }

    private String getDefaultIfcName() {
        return "Base" + getSimpleName(modelName) + "Application";
    }

    private String initPkgName(String config) {
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

    public String getIfcPkgName(){
        return this.ifcPkgName;
    }

    public String getIfcName() {
        return this.ifcName;
    }

    public String getFullIfcName() {
        return ifcPkgName + "." + ifcName;

    }

    public String getImpName() {
        return this.impName;

    }

    public String getImpPkg(){
        return this.impPkgName;
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
