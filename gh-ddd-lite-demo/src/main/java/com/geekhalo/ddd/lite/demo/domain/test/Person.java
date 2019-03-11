package com.geekhalo.ddd.lite.demo.domain.test;

import lombok.Data;

import java.util.Date;

@Data
public class Person {
    private String name;
    private Date birthday;
    private Boolean enable;

    public static Person create(PersonCreator creator){
        Person person = new Person();
        person.setName(creator.getName());
        person.setBirthday(creator.getBirthday());
        person.setEnable(creator.getEnable());
        return person;
    }
}
