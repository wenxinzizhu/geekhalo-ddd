package com.geekhalo.ddd.lite.codegen.updater;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 收集public的字段和setter方法，并将其封装到BaseXXXXXUpdater中
 */
public @interface GenUpdater {
    String parent() default "";
}
