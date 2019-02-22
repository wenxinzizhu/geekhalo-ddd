//package com.whkj.infra.ddd.query.dto;
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//@ApiModel
//@Data
//public class PageableVo<Q> {
//    @ApiModelProperty("页号")
//    private int pageNo = 0;
//    @ApiModelProperty("每页大小")
//    private Integer pageSize = 100;
//    @ApiModelProperty("查询对象")
//    private Q query;
//
//    public Pageable toPageable(){
//        return PageRequest.of(getPageNo(), getPageSize());
//    }
//}
