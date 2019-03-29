package com.geekhalo.ddd.lite.codegen;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface ContainerManaged {
}
