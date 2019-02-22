package com.geekhalo.ddd.lite.demo.domain.news.info;

import com.geekhalo.ddd.lite.codegen.converter.GenCodeBasedEnumConverter;
import com.geekhalo.ddd.lite.domain.support.CodeBasedEnum;

@GenCodeBasedEnumConverter
public enum NewsInfoStatus implements CodeBasedEnum<NewsInfoStatus> {
    ENABLE(1),
    DISABLE(2);

    private final int code;

    NewsInfoStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
