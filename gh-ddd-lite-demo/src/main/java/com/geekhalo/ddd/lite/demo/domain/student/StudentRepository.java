package com.geekhalo.ddd.lite.demo.domain.student;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.application.GenApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@GenApplication
public interface StudentRepository extends BaseStudentRepository {

    @Override
    @Description("根据Id获取学生信息")
    Optional<Student> getById(Long id);

    @Override
    @Description("根据ParentId获取学生信息")
    Page<Student> findByParentId(Long parentId, Pageable pageable);

    @Override
    @Description("根据ParentId获取学生信息")
    List<Student> getByParentId(Long parentId);

    @Override
    @Description("根据ParentId获取学生数量")
    Long countByParentId(Long parentId);
}
