package com.geekhalo.ddd.lite.demo.application;


import com.geekhalo.ddd.lite.demo.domain.student.Student;
import com.geekhalo.ddd.lite.demo.domain.student.StudentCreator;
import com.geekhalo.ddd.lite.demo.domain.student.StudentUpdater;
import com.geekhalo.ddd.lite.demo.domain.student.StudentDto;
import com.geekhalo.ddd.lite.codegen.controller.GenController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@GenController("com.geekhalo.ddd.lite.demo.controller.BaseStudentController")
public interface StudentApplication extends BaseStudentApplication{
    @Override
    Student create(StudentCreator creater);

    @Override
    void enable(Long id);

    @Override
    void cancel(Long id, String reason);

    @Override
    void disable(Long id);

    @Override
    void update(Long id, StudentUpdater updater);

    @Override
    void methodInPerson4(Long id);

    @Override
    void methodInPerson3(Long id);

    @Override
    void methodInPerson2(Long id);

    @Override
    void methodInPerson1(Long id);

    @Override
    List<StudentDto> getByParentId(Long parentId);

    @Override
    Long countByParentId(Long parentId);

    @Override
    Page<StudentDto> findByParentId(Long parentId, Pageable pageable);

    @Override
    Optional<StudentDto> getById(Long id);

}
