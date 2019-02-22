package com.geekhalo.ddd.lite.codegen.application;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface GenApplicationIgnore {
}
