package com.geekhalo.ddd.lite.codegen.springdatarepository;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
/**
 * 默认读取Index相关配置，自动生成BaseXXXXRepository
 */
public @interface GenSpringDataRepository {
    String pkgName() default "";
    String idClsName() default "";
    String clsName() default "";
    boolean useQueryDsl() default true;
}
