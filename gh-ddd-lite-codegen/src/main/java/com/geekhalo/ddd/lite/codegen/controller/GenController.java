package com.geekhalo.ddd.lite.codegen.controller;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface GenController {
    String value();
    String parentClass() default "";
}
