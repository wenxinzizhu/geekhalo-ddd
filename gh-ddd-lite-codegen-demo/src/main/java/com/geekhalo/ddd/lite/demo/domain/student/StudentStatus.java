package com.geekhalo.ddd.lite.demo.domain.student;

import com.geekhalo.ddd.lite.codegen.converter.GenCodeBasedEnumConverter;
import com.geekhalo.ddd.lite.domain.support.CodeBasedEnum;
import com.geekhalo.ddd.lite.domain.support.SelfDescribedEnum;

@GenCodeBasedEnumConverter()
public enum StudentStatus implements CodeBasedEnum<StudentStatus>,SelfDescribedEnum {
    ;
    private final int code;
    private final String descr;
    StudentStatus(int code, String descr) {
        this.code = code;
        this.descr = descr;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return this.descr;
    }
}
