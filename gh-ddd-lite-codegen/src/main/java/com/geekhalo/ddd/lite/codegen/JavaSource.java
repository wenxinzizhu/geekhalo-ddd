package com.geekhalo.ddd.lite.codegen;

import com.google.common.collect.Sets;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Value
public class JavaSource{
    private final Set<String> fields = Sets.newHashSet();
    private final Set<String> methods = Sets.newHashSet();
    private final String pkgName;
    private final String clsName;
    @Getter(AccessLevel.PRIVATE)
    private final TypeSpec.Builder typeSpecBuilder;

    public String getFullName() {
        return pkgName + "." +clsName;
    }

    public TypeSpec getTypeSpec(){
        return getTypeSpecBuilder().build();
    }

    public void addMethod(MethodSpec method){
        String key = createMethodKey(method);
        if (this.methods.contains(key)){
            throw new RuntimeException(String.format("Repetition Method %s", key));
        }
        this.typeSpecBuilder.addMethod(method);
        this.methods.add(key);
    }

    public void addField(FieldSpec fieldSpec){
        String key = fieldSpec.name;
        if (this.fields.contains(key)){
            throw new RuntimeException(String.format("Repetition Field %s", key));
        }
        this.typeSpecBuilder.addField(fieldSpec);
        this.fields.add(key);
    }

    public boolean hasField(String field){
        return fields.contains(field);
    }

    private String createMethodKey(MethodSpec method) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method.name);
        if (CollectionUtils.isEmpty(method.parameters)){
            stringBuilder.append("()");
        }else {
            stringBuilder.append("(");
            stringBuilder.append(method.parameters.stream()
                    .map(parameterSpec -> parameterSpec.type)
                    .map(typeName -> typeName.toString())
                    .collect(Collectors.joining(","))
            );
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }

    public void addType(TypeSpec type) {
        this.typeSpecBuilder.addType(type);
    }
}