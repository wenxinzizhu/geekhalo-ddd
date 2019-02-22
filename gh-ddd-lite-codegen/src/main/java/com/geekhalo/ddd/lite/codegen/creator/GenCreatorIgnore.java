package com.geekhalo.ddd.lite.codegen.creator;


import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface GenCreatorIgnore {
}
