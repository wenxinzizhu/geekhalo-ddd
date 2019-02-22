package com.geekhalo.ddd.lite.domain;

public class BizBeanValidationException extends BusinessException {
    private String msg;

    public BizBeanValidationException(String msg){
        super(404, msg);
        this.msg  = msg;
    }
}
