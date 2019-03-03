package com.geekhalo.ddd.lite.spring.mvc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ApiModel
@Data
public class PageableImpl
        implements Pageable{

    @ApiModelProperty("页号")
    private int pageNo = 0;

    @ApiModelProperty("每页大小")
    private int pageSize = 100;

    public Pageable toPageable(){
        return PageRequest.of(getPageNo(), getPageSize());
    }

    @Override
    @JsonIgnore
    public int getPageNumber() {
        return pageNo;
    }

    @Override
    @JsonIgnore
    public long getOffset() {
        return (long)this.pageNo * (long)this.pageSize;
    }

    @Override
    @JsonIgnore
    public Sort getSort() {
        return Sort.unsorted();
    }

    @Override
    @JsonIgnore
    public Pageable next() {
        return null;
    }

    @Override
    @JsonIgnore
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    @JsonIgnore
    public Pageable first() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean hasPrevious() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isPaged() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isUnpaged() {
        return false;
    }

    @Override
    @JsonIgnore
    public Sort getSortOr(Sort sort) {
        return null;
    }
}
