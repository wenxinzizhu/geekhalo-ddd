package com.geekhalo.ddd.lite.demo.domain.mixed;

import com.geekhalo.ddd.lite.codegen.application.GenApplicationMethod;
import com.geekhalo.ddd.lite.codegen.application.GenMixedApplication;

import java.util.Optional;

@GenMixedApplication(implementName = "BaseAuthApplication")
public interface MobileAuthRepository extends BaseMobileAuthRepository{
    @GenApplicationMethod(name = "getMobileById")
    @Override
    Optional<MobileAuth> getById(Long aLong);
}
