package com.geekhalo.ddd.lite.codegen.application;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface GenSingleApplication {
    String pkgName() default "";
    String interfaceName() default "";
    String implementName() default "";
    String superClassName() default "";
    boolean genIfc() default true;
    boolean genImpl() default true;
}
