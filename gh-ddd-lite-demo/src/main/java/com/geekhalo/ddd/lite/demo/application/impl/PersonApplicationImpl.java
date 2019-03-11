package com.geekhalo.ddd.lite.demo.application.impl;

import com.geekhalo.ddd.lite.demo.application.PersonApplication;
import com.geekhalo.ddd.lite.demo.domain.test2.Person;
import com.geekhalo.ddd.lite.demo.domain.test2.PersonDto;
import org.springframework.stereotype.Service;

@Service
public class PersonApplicationImpl extends BasePersonApplicationSupport
        implements PersonApplication {
    @Override
    protected PersonDto convertPerson(Person src) {
        return new PersonDto(src);
    }
}
