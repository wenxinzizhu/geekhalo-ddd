package com.geekhalo.ddd.lite.demo.domain.test2;

import com.geekhalo.ddd.lite.codegen.converter.GenCodeBasedEnumConverter;
import com.geekhalo.ddd.lite.domain.support.CodeBasedEnum;

@GenCodeBasedEnumConverter
public enum PersonStatus implements CodeBasedEnum<PersonStatus> {
    ENABLE(1), DISABLE(0);

    private final int code;

    PersonStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
