package com.geekhalo.ddd.lite.codegen;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface QueryMethod {
}
