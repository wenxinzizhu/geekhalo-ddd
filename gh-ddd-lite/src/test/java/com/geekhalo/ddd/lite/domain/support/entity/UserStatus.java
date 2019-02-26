package com.geekhalo.ddd.lite.domain.support.entity;

import com.geekhalo.ddd.lite.domain.support.CodeBasedEnum;

public enum UserStatus implements CodeBasedEnum<UserStatus> {
    ENABLE(1),
    DISABLE(0);

    private final int code;

    UserStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
