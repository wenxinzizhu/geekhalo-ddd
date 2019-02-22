package com.geekhalo.ddd.lite.codegen.repository;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
@Repeatable(value = Indexes.class)
public @interface Index {
    String [] value();
    boolean unique() default false;
}
