package com.geekhalo.ddd.lite.demo.domain.test2;

import com.geekhalo.ddd.lite.codegen.application.GenApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@GenApplication
public interface PersonRepository extends BasePersonRepository{
    @Override
    Page<Person> findByName(String name, Pageable pageable);

    @Override
    Page<Person> findByNameAndStatus(String name, PersonStatus status, Pageable pageable);
}
