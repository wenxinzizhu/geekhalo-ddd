package com.geekhalo.ddd.lite.demo.domain.test2;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.application.GenApplication;
import com.geekhalo.ddd.lite.codegen.creator.GenCreator;
import com.geekhalo.ddd.lite.codegen.dto.GenDto;
import com.geekhalo.ddd.lite.codegen.repository.Index;
import com.geekhalo.ddd.lite.codegen.springdatarepository.GenSpringDataRepository;
import com.geekhalo.ddd.lite.codegen.updater.GenUpdater;
import com.geekhalo.ddd.lite.domain.support.jpa.JpaAggregate;
import com.geekhalo.ddd.lite.domain.support.mongo.MongoAggregate;
import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Convert;
import java.util.Date;

@GenUpdater
@GenCreator
@GenDto
@GenSpringDataRepository
@Index({"name", "status"})
@QueryEntity

@GenApplication

@Document
@Data
public class Person extends MongoAggregate {
    @Description("名称")
    private String name;
    @Setter(AccessLevel.PROTECTED)
    private Date birthday;
    private Boolean enable;

    @Convert(converter = CodeBasedPersonStatusConverter.class)
    private PersonStatus status;

    public static Person create(PersonCreator creator){
        Person person = new Person();
        creator.accept(person);
        return person;
    }

    public void update(PersonUpdater updater){
        updater.accept(this);
    }

    public void enable(){
        setStatus(PersonStatus.ENABLE);
    }

    public void disable(){
        setStatus(PersonStatus.DISABLE);
    }
}
