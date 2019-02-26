package com.geekhalo.ddd.lite.codegen;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface EnableGenForAggregate {
    String idClsName() default "";
}
