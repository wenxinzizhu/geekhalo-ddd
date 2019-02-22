package com.geekhalo.ddd.lite.demo.application.impl;

import com.geekhalo.ddd.lite.demo.application.StudentApplication;
import com.geekhalo.ddd.lite.demo.domain.student.Student;
import com.geekhalo.ddd.lite.demo.domain.student.StudentDto;
import org.springframework.stereotype.Service;

@Service
public class StudentApplicationImpl extends BaseStudentApplicationSupport
    implements StudentApplication {

    @Override
    protected StudentDto convertStudent(Student src) {
        return new StudentDto(src);
    }
}
