package com.geekhalo.ddd.lite.demo.domain.student;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.codegen.creator.GenCreatorIgnore;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaAggregate;
import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@EnableGenForAggregate
@MappedSuperclass
public abstract class Person extends JpaAggregate {
    @Description("姓名")
    private String name;
    @Description("年龄")
    @GenCreatorIgnore
    private Long age;

    @Description("methodInPerson1")
    public void methodInPerson1(){

    }

    @Description("methodInPerson2")
    public void methodInPerson2(){

    }

    @Description("methodInPerson3")
    public void methodInPerson3(){

    }

    @Description("methodInPerson4")
    public void methodInPerson4(){

    }
}
