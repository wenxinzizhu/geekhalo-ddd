package com.geekhalo.ddd.lite.codegen.updater;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenUpdaterIgnore {
}
