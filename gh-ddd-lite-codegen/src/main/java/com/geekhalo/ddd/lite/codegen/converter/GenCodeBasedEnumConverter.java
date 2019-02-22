package com.geekhalo.ddd.lite.codegen.converter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
/**
 * 生成JPA枚举转化器
 */
public @interface GenCodeBasedEnumConverter {
    String pkgName() default "";
}
