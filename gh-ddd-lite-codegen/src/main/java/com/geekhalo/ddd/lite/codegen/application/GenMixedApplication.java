package com.geekhalo.ddd.lite.codegen.application;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface GenMixedApplication {
    String superClassName() default "";

    boolean genIfc() default false;
    String ifcPkgName() default "";
    String interfaceName() default "";

    boolean genImpl() default true;
    String implPkgName() default "";
    String implementName() default "";

}
