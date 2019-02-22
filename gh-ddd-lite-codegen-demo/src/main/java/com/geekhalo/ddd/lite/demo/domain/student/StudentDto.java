package com.geekhalo.ddd.lite.demo.domain.student;


import com.geekhalo.ddd.lite.demo.domain.EmailDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class StudentDto extends BaseStudentDto {
    private List<EmailDto> emailDtos;
//
    public StudentDto(Student source) {
        super(source);
        this.emailDtos = source.getEmails().stream()
            .map(email -> new EmailDto(email))
            .collect(toList());
    }
}
