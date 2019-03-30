package com.geekhalo.ddd.lite.demo.domain.mixed;

import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.application.GenApplicationMethod;
import com.geekhalo.ddd.lite.codegen.application.GenMixedApplication;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaAggregate;
@EnableGenForAggregate
@GenMixedApplication(implementName = "BaseAuthApplication")
public class MobileAuth extends JpaAggregate {

    @GenApplicationMethod(methodName = "createMobile")
    public static MobileAuth createMobileAuth(String mobile){
        return new MobileAuth();
    }
}
