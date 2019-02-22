package com.geekhalo.ddd.lite.codegen.controller;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface GenControllerIgnore {
}
