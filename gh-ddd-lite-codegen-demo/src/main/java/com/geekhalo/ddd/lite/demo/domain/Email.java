package com.geekhalo.ddd.lite.demo.domain;

import com.geekhalo.ddd.lite.codegen.EnableGenForEntity;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaEntity;
import lombok.Data;

@EnableGenForEntity
@Data
public class Email extends JpaEntity {
    private String name;
    private String domain;
}
