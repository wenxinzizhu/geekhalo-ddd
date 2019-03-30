package com.geekhalo.ddd.lite.demo.domain.mixed;

import com.geekhalo.ddd.lite.codegen.ContainerManaged;
import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.application.GenApplicationMethod;
import com.geekhalo.ddd.lite.codegen.application.GenMixedApplication;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaAggregate;
import org.springframework.context.ApplicationEventPublisher;

@EnableGenForAggregate
@GenMixedApplication(implementName = "BaseAuthApplication")
public class EmailAuth extends JpaAggregate {

    @GenApplicationMethod(methodName = "createEmail")
    public static EmailAuth create(String email){
        return new EmailAuth();
    }


    public void update(String name, @ContainerManaged ApplicationEventPublisher applicationEventPublisher){

    }
}
