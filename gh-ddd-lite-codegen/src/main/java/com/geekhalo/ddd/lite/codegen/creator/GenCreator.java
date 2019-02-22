package com.geekhalo.ddd.lite.codegen.creator;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
/**
 * 收集非private的字段和setter方法，将其封装成BaseXXXXCreator
 */
public @interface GenCreator {
    String parent() default "";
    GenCreatorType type() default GenCreatorType.JAVA_BEAN;
}
