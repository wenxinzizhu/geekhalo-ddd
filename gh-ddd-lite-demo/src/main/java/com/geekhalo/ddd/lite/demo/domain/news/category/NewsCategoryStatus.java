package com.geekhalo.ddd.lite.demo.domain.news.category;

import com.geekhalo.ddd.lite.codegen.converter.GenCodeBasedEnumConverter;
import com.geekhalo.ddd.lite.domain.support.CodeBasedEnum;

/**
 * GenCodeBasedEnumConverter 自动生成 CodeBasedNewsCategoryStatusConverter 类
 */
@GenCodeBasedEnumConverter
public enum  NewsCategoryStatus implements CodeBasedEnum<NewsCategoryStatus> {
    ENABLE(1),
    DISABLE(0);

    private final int code;

    NewsCategoryStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
