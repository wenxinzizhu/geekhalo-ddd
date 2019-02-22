package com.geekhalo.ddd.lite.codegen.repository;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface Indexes {
    Index[] value();
}
