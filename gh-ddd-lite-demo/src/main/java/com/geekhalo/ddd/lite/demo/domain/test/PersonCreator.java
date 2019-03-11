package com.geekhalo.ddd.lite.demo.domain.test;

import lombok.Data;

import java.util.Date;

@Data
public class PersonCreator {
    private String name;
    private Date birthday;
    private Boolean enable;
}
