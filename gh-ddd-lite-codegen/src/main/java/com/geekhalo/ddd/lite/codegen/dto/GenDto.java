package com.geekhalo.ddd.lite.codegen.dto;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 默认收集public的getter方法，并封装到XXXXVo中
 */
public @interface GenDto {
    String pkgName() default "";

    String parent() default DtoConstants.JPA_AGG_VO;
}
