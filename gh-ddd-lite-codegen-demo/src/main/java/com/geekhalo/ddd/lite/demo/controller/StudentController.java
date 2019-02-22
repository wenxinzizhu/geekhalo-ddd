package com.geekhalo.ddd.lite.demo.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("student")
public class StudentController extends BaseStudentController{

    @RequestMapping(value = "test",method = RequestMethod.GET)
    @ApiOperation("测试")
    public String test(){
        return "test";
    }
}
