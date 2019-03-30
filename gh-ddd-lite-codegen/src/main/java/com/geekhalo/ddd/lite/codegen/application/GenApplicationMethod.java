package com.geekhalo.ddd.lite.codegen.application;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface GenApplicationMethod {
    String name() default "";
}
