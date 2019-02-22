package com.geekhalo.ddd.lite.demo.domain.student;

import com.geekhalo.ddd.lite.codegen.Description;
import com.geekhalo.ddd.lite.codegen.EnableGenForAggregate;
import com.geekhalo.ddd.lite.demo.domain.Email;
import com.querydsl.core.annotations.QueryEntity;

import com.geekhalo.ddd.lite.codegen.creator.GenCreator;
import com.geekhalo.ddd.lite.codegen.creator.GenCreatorIgnore;
import com.geekhalo.ddd.lite.codegen.creator.GenCreatorType;
import com.geekhalo.ddd.lite.codegen.repository.Index;
import com.geekhalo.ddd.lite.codegen.springdatarepository.GenSpringDataRepository;
import com.geekhalo.ddd.lite.codegen.dto.GenDtoIgnore;
import com.geekhalo.ddd.lite.codegen.dto.GenDtoPropertyConvert;
import com.geekhalo.ddd.lite.codegen.dto.GenDtoPropertyModel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data

@EnableGenForAggregate

@Index({"parentId"})
@Index({"userId"})
@Index({"userId", "parentId"})
@Index({"userId", "uuid"})
@Index(value = {"uuid"}, unique = true)
@Index({"parentId", "name"})

@QueryEntity
@Entity
@Table(name = "tb_student")
public class Student extends Person {
    private static final Logger LOGGER = LoggerFactory.getLogger(Student.class);

    @Setter(AccessLevel.PROTECTED)
    @Description("父ID")
    private Long parentId;

    @Description("用户ID")
    @Setter(AccessLevel.PROTECTED)
    private Long userId;

    @Description("唯一ID")
    @Setter(AccessLevel.PROTECTED)
    private String uuid;

    @Description("状态")
    @Setter(AccessLevel.PRIVATE)
    @Convert(converter = CodeBasedStudentStatusConverter.class)
    @GenDtoPropertyConvert(GenDtoPropertyModel.TO_SELF_DESCRIBED_VO)
    private StudentStatus status;

    @Description("爱好")
    @Setter(AccessLevel.PROTECTED)
    private String hobby;

    @GenDtoPropertyConvert(GenDtoPropertyModel.INSTANT_TO_LONG)
    private Instant instant;

    private BigDecimal salary;

    private String address;

    private Integer property;

    @GenDtoIgnore
    @GenCreatorIgnore
    @Transient
    private List<Email> emails;

    public boolean isSuccess(){
        return true;
    }

    @Description("处理结果")
    public Boolean getResult(){
        return false;
    }

    @Description("创建学生")
    public static Student create(@Description("creator") StudentCreator creater){
        Student student = new Student();
        creater.accept(student);
        return student;
    }

    @Description("更新学生")
    public void update(@Description("updater") StudentUpdater updater){
        updater.accept(this);
    }

    @Description("启用")
    public void enable(){
//        this.status = StudentStatus.ENABLE;
    }

    @Description("禁用")
    public void disable(){

    }

    @Description("取消")
    public void cancel(@Description("取消原因") String reason){

    }
}
